package com.example.demo.services;
import java.util.List;
import java.util.Optional;

import com.example.demo.models.User;

public interface UserService {
	User addUser(User user);
	User getUserById(Integer id);
	List<User> getUsers(String lastName);
}
