package com.example.demo.controllers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FileController {
	private Logger logger = LoggerFactory.getLogger(getClass());
	//	@Autowired
//	private FileService fileService; 
	@Value("${config.amqp.exchange}")
	private String exchangeName;
	// just test rabbit, will add parameters later. 	
	@Autowired	
	private RabbitTemplate rabbitTemplate;
	@PostMapping("/addfile")
	public String addFile(){
		try {
			rabbitTemplate.convertAndSend(exchangeName, "hello", "test rabbit");
			return "message sent"; 	
		}catch(Exception e) {
			logger.error(e.getMessage());
			return e.getMessage();
		}
		
	}
}
