package com.example.demo.services;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;

import java.util.Optional;
public interface FileService {
    Flux<String> save(Integer userId, Flux<FilePart> files);
    Flux<String> find(Integer userId, Optional<String> tag);
}
