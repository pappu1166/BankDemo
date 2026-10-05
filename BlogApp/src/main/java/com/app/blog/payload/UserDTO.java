package com.app.blog.payload;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.validator.constraints.Email;

import com.app.blog.entities.Role;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserDTO {
	private int id;
	@NotEmpty
	@Size(min = 4,message = "Name should not be less than 4 characters !!")
	
	private String name;
	
	@Email(message = "Email address is not valid !!")
	private String email;
	@NotEmpty
	@Size(min = 3,max = 10,message = "Password shoild be lies between 3 to 10 characters !!")
	private String password;
	@NotEmpty
	private String about;
	
	private Set<RoleDto> roles = new HashSet<>();
}
