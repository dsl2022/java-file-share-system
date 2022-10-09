package com.example.demo.services.impl;

import com.example.demo.models.mongodb.File;
import com.example.demo.repositories.mongodb.FileRepository;
import com.example.demo.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

public class FileServiceImpl<fileRepository> implements FileService {
    @Autowired
    private FileRepository fileRepository;

    @Override
    public Flux<String> save(Integer userId, Flux<FilePart> files) {
        return files.map(filePart -> {
            File file = new File();
            String fileName = filePart.filename();
            String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
            file.setFileName(fileName);
            //TODO implement twitter snowflake id generation
            file.setId(1);
            file.setTags(Arrays.asList("lifestyle", "travel"));
            file.setKey("testKey");
            file.setShared(false);
            file.setCreatedAt(LocalDateTime.now());
            return file;
            // TODO to save to disk, push to s3 and to db
            }).flatMap(file -> fileRepository.save(file)).map(file -> file.getKey());
    }

    @Override
    public Flux<String> find(Integer userId, Optional<String> tag) {
        return null;
    }
}
