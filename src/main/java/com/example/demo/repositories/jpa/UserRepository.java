package com.example.demo.repositories.jpa;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import com.example.demo.models.rds.User;

public interface UserRepository extends CrudRepository<User,Integer> {
	List<User> findUsersByLastName(String lastName);
	Optional<User> findUserByEmail(String email);	
}
