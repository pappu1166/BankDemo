package com.app.blog.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.app.blog.payload.UserDTO;

public interface UserService {

	UserDTO resigsterNewUser(UserDTO userDTO);

	UserDTO createUser(UserDTO user);

	UserDTO updateUser(UserDTO user, Integer userid);

	UserDTO getUserById(Integer userid);

	List<UserDTO> getAllUsers();

	void deleteUser(Integer userid);
}
