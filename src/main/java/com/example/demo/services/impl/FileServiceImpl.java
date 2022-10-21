package com.example.demo.services.impl;

import com.example.demo.exceptions.StorageException;
import com.example.demo.models.mongodb.File;
import com.example.demo.repositories.mongodb.FileRepository;
import com.example.demo.services.FileService;
import com.example.demo.services.IdGenerator;
import com.example.demo.utils.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

@Service

public class FileServiceImpl<fileRepository> implements FileService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private FileRepository fileRepository;
    @Value("${storage.local.path:/tmp/demo}")
    private String directory;

    @Value("${storage.aws.region:us-east-1}")
    private String region;
    @Value("${storage.aws.bucket:file-sync-project}")
    private String bucket;
    @Autowired
    private IdGenerator idGenerator;
    private S3Client s3;

    @PostConstruct
    public void init(){
        ProfileCredentialsProvider credentialsProvider = ProfileCredentialsProvider.create();
        s3 = S3Client.builder().region(Region.of(region)).credentialsProvider(credentialsProvider).build();
    }

    @Override
    public Flux<String> save(Long userId, Flux<FilePart> files) {
        return files
                .map(filePart -> {
                    Path path = Paths.get(directory, userId.toString());
                    if (Files.notExists(path)) {
                        try {
                            Files.createDirectory(path);
                        } catch (IOException e) {
                            throw new StorageException(e);
                        }
                    }
                    if (!Files.isDirectory(path)) {
                        throw new StorageException(String.format("Can not create a directory as there is a file {} exists", path.getFileName()));
                    }
                    java.io.File newFile = new java.io.File(path.resolve(filePart.filename()).toUri());
                    if (newFile.exists()) {
                        newFile = new java.io.File(path.resolve(RandomUtil.generateRandomString(6) + filePart.filename()).toUri());
                    }
                    return filePart.transferTo(newFile).thenReturn(newFile);
                })
                .flatMap(newFile -> {
                    // upload to S3
                    return newFile.map(file -> {
                        String key = userId.toString() + RandomUtil.generateRandomString(10);
                        PutObjectResponse response = s3.putObject(PutObjectRequest.builder().bucket(bucket).key(key).build(), RequestBody.fromFile(file));
                        Map<String, String> map = new HashMap<>();
                                map.put("name", file.getName());
                                map.put("key", key);
                                map.put("eTag", response.eTag());

                                map.put("url", String.format("https://%s.s3.%s.amazonaws.com/%s", bucket,region,key));
                                return map;
                            });
                        }
                ).map(map -> {

            // TODO
            // case 1 when user upload the same file, by name, size, extension (we need to do something)
            // case 2 if file is good, just upload to s3.
            //
            File file = new File();
            String fileName = map.get("name");
            String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
            file.setFileName(fileName);
            //TODO implement twitter snowflake id generation
            file.setId(idGenerator.nextId());
            file.setTags(Arrays.asList("lifestyle", "travel"));
            file.setKey(map.get("key"));
            file.setUrl(map.get("url"));
            file.setShared(false);
            file.setCreatedAt(LocalDateTime.now());
            file.setUserId(userId);
            logger.debug("we get file {},with key {}", file.getFileName(), file.getKey());
            return file;
            // TODO to save to disk, push to s3 and to db
        }).flatMap(
                file -> fileRepository.save(file)
//                    logger.debug("we are going to save a file {} into mongodb,with key {}", file.getFileName(), file.getKey());
//
        ).map(file ->
//            logger.debug("we have saved the file {} into mongodb,with key {}", file.getFileName(), file.getKey());
            file.getKey()
        );
    }

    @Override
    public Flux<File> find(Long userId, List<String> tags) {
        // todo currently it is not really exactly and correctly filtering by tags, e.g. lifestyle & coding, would return records that includes cooking. we need to investigate.
        return tags.isEmpty()? fileRepository.findByUserId(userId):fileRepository.findByUserIdAndTagsIn(userId,tags);
    }
}
