package com.example.demo.dto;

import java.util.List;

public record UpdateFile(String fileName, List<String> tags, Boolean isShared) {

}
