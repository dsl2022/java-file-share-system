package com.example.demo.repositories.mongodb;

import com.example.demo.models.mongodb.File;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface FileRepository extends ReactiveCrudRepository<File,Integer> {

}
