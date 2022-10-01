package com.example.demo.repositories;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import com.example.demo.models.User;

public interface UserRepository extends CrudRepository<User,Integer> {

	List<User> findUsersByLastName(String lastName);
	
}
