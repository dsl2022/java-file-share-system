package com.example.demo.services.impl;

import java.util.List;
import java.util.Optional;

import com.example.demo.dto.JwtUserPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.models.rds.User;
import com.example.demo.repositories.jpa.UserRepository;
import com.example.demo.services.UserService;

@Service
public class UserServiceImpl implements UserService {
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder; 
	@Override
	@Transactional
	@CachePut(value="user", key = "T(String).valueOf(#user.id)")
	public User addUser(User user) {
		logger.info("test if cache write is hit");
		
		Optional<User> searchUser = userRepository.findById(user.getId());		
		if(searchUser.isEmpty()) {
			 String password = user.getPassword();			
			user.setPassword(passwordEncoder.encode(password));
			userRepository.save(user);
			return user;
		}		
		return  null;
	}

	@Override
	@Cacheable(value = "user", key = "T(String).valueOf(#id)")
	public User getUserById(Integer id) {
		logger.info("test if cache read is not hit");
		Optional<User> searchUser = userRepository.findById(id);
		
		if(searchUser.isPresent()) {
			return searchUser.get();
		}else {
			// refactor this, cache does not like null and if adding same user again it will fail.  
			return null;
		}
	}

	@Override
	@Cacheable(value = "user", key="{#lastName}")
	public List<User> getUsers(String lastName) {		
		logger.info("load from db");
		return (List<User>) userRepository.findUsersByLastName(lastName);
	}

	@Override
	public boolean verify(JwtUserPayload jwtUserPayload) {
		User foundUser = getUserById(jwtUserPayload.getId());
		if(foundUser != null){
			User tempUser = new User();
			BeanUtils.copyProperties(jwtUserPayload,tempUser);
			tempUser.setFirstName(foundUser.getFirstName());
			tempUser.setLastName(foundUser.getLastName());
			return tempUser.equals(foundUser);
		}
		return false;
	}
	// for update and delete	
	//	@CacheEvict(value="user",key = "T(String).valueOf(#user.id)")

}
