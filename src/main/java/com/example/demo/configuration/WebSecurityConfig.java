package com.example.demo.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class WebSecurityConfig {
	@Value("${spring.security.user.name}")
	private String username;
	@Value("${spring.security.user.password}")
	private String password;
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Bean
    public MapReactiveUserDetailsService userDetailsService() {
//        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    	
    	UserDetails user = User.withUsername(username).password(passwordEncoder().encode(password))
                .roles("ACTUATOR", "USER")
                .build();
        return new MapReactiveUserDetailsService(user);
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
    	return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
   
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
    	logger.info("initialized spring web config");
        return http
                .csrf().disable()
                .authorizeExchange()
                .pathMatchers("/actuator/**").hasRole("ACTUATOR")
                .pathMatchers("/api/**")
                .hasRole("USER")
                .pathMatchers("/public/api/**").permitAll()
                .and()                
                .formLogin()
                .and()
                .oauth2Login()                
                .and().build();
    }
}
