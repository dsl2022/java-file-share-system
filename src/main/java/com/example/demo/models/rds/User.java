package com.example.demo.models.rds;
 // Serializable is only good for Java project
//import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Entity
@Data
public class User {
//public class User implements Serializable {
//	private static final long serialVersionUID = -7299940417667088078L;
	@Id
	private Long id;
	private String firstName;
	private String lastName;
	private String email;
	private String username;
//	@JsonIgnore
	private String password;

	@Override
	public int hashCode() {
		return Objects.hash(email, firstName, id, lastName, username);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(email, other.email) && Objects.equals(firstName, other.firstName)
				&& Objects.equals(id, other.id) && Objects.equals(lastName, other.lastName)
				&& Objects.equals(username, other.username);
	}
//	public String getPassword() {
//		return password;
//	}
//	public void setPassword(String password) {
//		this.password = password;
//	}
	
}
