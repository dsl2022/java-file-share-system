package com.example.demo.services;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import com.example.demo.DemoApplication;
import com.example.demo.models.rds.User;


@TestPropertySource("/application-test.yaml")
@ActiveProfiles("test")
@SpringBootTest(classes = DemoApplication.class)
public class UserServiceTest {
	@Autowired
	private UserService userService;
	@Test
    public void testList(){
        User user = new User();        
        user.setId(1);
        user.setEmail("ayden.franklin@test.com");
        user.setFirstName("John");
        user.setLastName("Philip");
        user.setUsername("ddl");
        userService.addUser(user);        
        User testUser = userService.getUserById(user.getId());
        assertTrue(testUser!=null);
        assertEquals(testUser, user);
    }
	
	@Test
	public void testListUser() {
		System.out.println(userService.getUsers("david"));
		
	};	
}
