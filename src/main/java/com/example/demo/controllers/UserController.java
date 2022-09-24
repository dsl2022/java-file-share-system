package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.User;
import com.example.demo.services.UserService;

@RestController
public class UserController {
	@Autowired
	private UserService userService; 
	@PostMapping("/adduser")
	public String addUser(@RequestBody User user){
		return userService.addUser(user);
		 
	}
}
