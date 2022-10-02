package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.User;
import com.example.demo.services.UserService;

@RestController
public class UserController {
	@Autowired
	private UserService userService; 
	@PostMapping("/api/adduser")
	public String addUser(@RequestBody User user){
		if(userService.addUser(user)!=null) {
			return "User is added";
		}
		return "User exists";	 
	}
	@GetMapping("/api/getuser/{id}")
	public User getUserById(@PathVariable Integer id){			
			 return userService.getUserById(id);
	}
	@GetMapping("/api/getusers")
	public List<User> getUsers(@RequestParam String lastName){
		return userService.getUsers(lastName);
	}
}
