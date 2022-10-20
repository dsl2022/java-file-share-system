package com.example.demo.services;
import java.util.List;

import com.example.demo.dto.JwtUserPayload;
import com.example.demo.dto.RegisterDto;
import com.example.demo.models.rds.User;

public interface UserService {
	User addUser(RegisterDto user);
	User getUserById(Long id);
	List<User> getUsers(String lastName);
	boolean verify(JwtUserPayload jwtUserPayload);
}
