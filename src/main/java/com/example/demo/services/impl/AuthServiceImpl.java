package com.example.demo.services.impl;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.example.demo.dto.JwtUserPayload;
import com.example.demo.dto.LoginDto;
import com.example.demo.services.AuthService;
import com.example.demo.utils.JWTUtils;
import com.example.demo.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.UserService;

@Service
public class AuthServiceImpl implements AuthService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String login(LoginDto loginDto) {
        Optional<User> foundUser = userRepository.findUserByEmail(loginDto.getEmail());
        if (foundUser.isPresent()) {
            User user = foundUser.get();
            passwordEncoder.matches(loginDto.getPassword(), user.getPassword());
            logger.debug("get user before if");
            if (passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
                logger.debug("get user after if");
// copyProperties method will be dleted afer taking note.
                //				JwtUserPayload payload = new JwtUserPayload();
                // copy properties from reference
//				BeanUtils.copyProperties(user,payload);
                logger.debug("inside try");
                return JWTUtils.generateToken(user.getId(), user.getUsername(), user.getEmail(), "abcfdklafaalfjaflkjejlkrekljealfkj");//
                //TODO send token back
                // TODO define db field for email to be unique
            }
        }
        return "failed";
    }
}
