package com.example.demo.dto;

import lombok.Data;
import lombok.Getter;

@Data
public class JwtUserPayload {
    private Long id;
    private String email;
    private String username;
}
