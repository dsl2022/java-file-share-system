package com.example.demo.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class WebSecurityConfig {
    private Logger logger = LoggerFactory.getLogger(getClass());
    
   
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
    	logger.info("initialized spring web config");
        return http
                .csrf().disable()
                .authorizeExchange()
                .pathMatchers("/actuator/**").hasRole("ACTUATOR")
                .anyExchange().permitAll()
                .and()                
                .httpBasic().and().build();
    }
}
