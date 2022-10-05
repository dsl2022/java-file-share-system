package com.example.demo.services;
import java.util.List;

import com.example.demo.dto.JwtUserPayload;
import com.example.demo.models.User;

public interface UserService {
	User addUser(User user);
	User getUserById(Integer id);
	List<User> getUsers(String lastName);
	boolean verify(JwtUserPayload jwtUserPayload);
}
