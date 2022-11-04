package com.example.demo.controllers;
import com.example.demo.dto.JwtUserPayload;
import com.example.demo.dto.UpdateFile;
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
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.security.sasl.AuthorizeCallback;
import java.util.*;

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
	@PostMapping("/api/file")
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
	@PutMapping("api/file/{id}")
	public Mono<Void> updateFile(Authentication auth, @PathVariable long id, @RequestBody UpdateFile updateFile){
		return fileService.update(((JwtUserPayload)auth.getPrincipal()).getId(),id, updateFile);
	}
	@DeleteMapping("api/file/{id}")
	public Mono<Void> deleteFile(Authentication auth, @PathVariable long id){
		return fileService.delete(((JwtUserPayload)auth.getPrincipal()).getId(),id);
	}
	@GetMapping("/api/files")
	public Flux<File> listFiles(Authentication auth, @RequestParam(value="tag", required=false) List<String> searchTags){
		logger.debug("test search tags {}",searchTags);

	return fileService.find(((JwtUserPayload)auth.getPrincipal()).getId(),searchTags == null? Collections.emptyList(): searchTags);

	}
}
