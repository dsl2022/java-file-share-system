package com.example.demo.services.impl;

import java.util.Optional;

import com.example.demo.dto.LoginDto;
import com.example.demo.services.AuthService;
import com.example.demo.utils.JWTUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.models.rds.User;
import com.example.demo.repositories.jpa.UserRepository;

@Service
public class AuthServiceImpl implements AuthService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String login(LoginDto loginDto) {
        Optional<User> foundUser = userRepository.findOneByEmail(loginDto.getEmail());
        if (foundUser.isPresent()) {
            User user = foundUser.get();
            passwordEncoder.matches(loginDto.getPassword(), user.getPassword());
            logger.debug("get user before if");
            if (passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
                logger.debug("get user after if");
                logger.debug("inside try");
                return JWTUtils.generateToken(user.getId(), user.getUsername(), user.getEmail());
            }
        }
        return "failed";
    }
}
