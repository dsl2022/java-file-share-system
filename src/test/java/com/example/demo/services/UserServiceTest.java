package com.example.demo.services;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.demo.dto.RegisterDto;
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
        // TODO add in memory db
//        shift + f6 refractor variables for all occurances
        RegisterDto registerDto = new RegisterDto();
//        user.setId(1);
        registerDto.setEmail("ayden.franklin@test.com");
        registerDto.setFirstName("John");
        registerDto.setLastName("Philip");
        registerDto.setUsername("ddl");
        registerDto.setPassword("12345");
        User testUser = userService.addUser(registerDto);
        assertTrue(testUser!=null);
        assertEquals(testUser.getUsername(), registerDto.getUsername());
    }

	@Test
	public void testListUser() {
		System.out.println(userService.getUsers("david"));

	};
}
