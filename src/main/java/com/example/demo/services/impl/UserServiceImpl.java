package com.example.demo.services.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.UserService;

@Service
public class UserServiceImpl implements UserService {
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private UserRepository userRepository;
	@Override
	@Transactional
	@CachePut(value="user", key = "T(String).valueOf(#user.id)")
	public String addUser(User user) {
		logger.info("test if cache write is hit");
		Optional<User> searchUser = userRepository.findById(user.getId());		
		if(searchUser.isEmpty()) {
			userRepository.save(user);
			return "User is added";
		}		
		return "User existed";
	}

	@Override
	@Cacheable(value = "user", key = "T(String).valueOf(#user.id)")
	public Optional<User> getUserById(Integer id) {
		logger.info("test if cache read is hit");
		Optional<User> searchUser = userRepository.findById(id);
		
		return searchUser;
	}

	@Override
	public List<User> getAllUsers() {		
		return (List<User>) userRepository.findAll();
	}
	// for update and delete	
	//	@CacheEvict(value="user",key = "T(String).valueOf(#user.id)")

}
