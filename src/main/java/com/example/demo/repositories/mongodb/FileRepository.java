package com.example.demo.repositories.mongodb;

import com.example.demo.models.mongodb.File;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.List;

public interface FileRepository extends ReactiveCrudRepository<File,Long> {
    Flux<File> findByUserId(Long userId);
    Flux<File> findByUserIdAndTagsIn(Long userId, List<String> tags);

}


