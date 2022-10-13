package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Component;

@SpringBootApplication
@Component
//@EnableCaching
public class DemoApplication {
public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}
