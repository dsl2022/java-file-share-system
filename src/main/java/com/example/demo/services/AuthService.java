package com.example.demo.services;

import com.example.demo.dto.LoginDto;

public interface AuthService {
	String login(LoginDto loginDto);
	
}
