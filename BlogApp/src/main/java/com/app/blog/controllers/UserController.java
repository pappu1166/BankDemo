package com.app.blog.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.blog.payload.ApiResponse;
import com.app.blog.payload.UserDTO;
import com.app.blog.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserService userService;

	// create user
	@PostMapping("/")
	public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) {

		UserDTO createdUserDTO = this.userService.createUser(userDTO);
		return new ResponseEntity<>(createdUserDTO, HttpStatus.CREATED);
	}

	// Update user
	@PutMapping("/update/{userId}")
	public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserDTO userDTO, @PathVariable("userId") Integer uId) {

		UserDTO updateUserDTO = this.userService.updateUser(userDTO, uId);
		return ResponseEntity.ok(updateUserDTO);
	}

	// Delete user only deteted by admin user
    @PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/delete/{userId}")
	public ResponseEntity<ApiResponse> deleteUser( @PathVariable("userId") Integer uId) {

		this.userService.deleteUser(uId);
		return new ResponseEntity<ApiResponse>(new ApiResponse("User deleted sucessfully", "Success"), HttpStatus.OK);
	}

	// getAll user

	@GetMapping("/getAlluser")
	public ResponseEntity<List<UserDTO>> getAllUser() {
		System.out.println("Response---"+this.userService.getAllUsers());
		return ResponseEntity.ok(this.userService.getAllUsers());
	}

	// getSingle user

	@GetMapping("/getuse/{userId}")
	public ResponseEntity<UserDTO> getAllUser(@PathVariable Integer userId) {
		

		return ResponseEntity.ok(this.userService.getUserById(userId));
	}

}
