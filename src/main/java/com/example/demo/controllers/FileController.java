package com.example.demo.controllers;
import com.example.demo.dto.JwtUserPayload;
import com.example.demo.models.mongodb.File;
import com.example.demo.models.rds.User;
import com.example.demo.services.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Optional;

@RestController
public class FileController {
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private FileService fileService;
	@Value("${config.amqp.exchange}")
	private String exchangeName;
	// just test rabbit, will add parameters later.
	@Autowired
	private RabbitTemplate rabbitTemplate;
	@PostMapping("/api/upload")
	public Flux<String> addFiles(Authentication auth, @RequestPart("file")Flux<FilePart> files){
	return fileService.save(((JwtUserPayload)auth.getPrincipal()).getId(),files);

//		try {
//			rabbitTemplate.convertAndSend(exchangeName, "hello", "test rabbit");
//			return "file has been accepted";
//		}catch(Exception e) {
//			logger.error(e.getMessage());
//			return e.getMessage();
//		}

	}

	@GetMapping("/api/files")
	public Flux<File> listFiles(Authentication auth){
	return fileService.find(1, Optional.empty());

	}
}
