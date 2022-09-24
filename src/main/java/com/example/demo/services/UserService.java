package com.example.demo.services;
import java.util.List;
import java.util.Optional;

import com.example.demo.models.User;

public interface UserService {
	String addUser(User user);
	Optional<User> getUserById(Integer id);
	List<User> getAllUsers();
}
