package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.LoginDto;
import com.example.demo.models.rds.User;
import com.example.demo.services.AuthService;
import com.example.demo.services.UserService;
@RestController
public class AuthController {
	@Autowired
	private UserService userService;
	@Autowired
	private AuthService authService;
	@PostMapping("/public/api/register")
	public String register(@RequestBody User user){
		if(userService.addUser(user)!=null) {
			return "User is added";
		}
		return "User exists";	 
	}
	@PostMapping("/public/api/login")
	public String login(@RequestBody LoginDto loginDto){

		return authService.login(loginDto);
	}
}
