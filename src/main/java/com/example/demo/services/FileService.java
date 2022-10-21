package com.example.demo.services;
import com.example.demo.models.mongodb.File;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Optional;
public interface FileService {
    Flux<String> save(Long userId, Flux<FilePart> files);
    Flux<File> find(Long userId, List<String> tag);
}
