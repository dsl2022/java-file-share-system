package com.example.demo.models.mongodb;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document
@Data
public class File {
    private Long id;
    private Long userId;
    private String key;
    private String url;
    private String fileName;
    private String extension;
    private boolean isShared;
    private List<String> tags;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
}
