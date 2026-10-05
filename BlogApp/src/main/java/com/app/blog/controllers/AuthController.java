package com.app.blog.controllers;

import java.lang.System.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.blog.exceptions.APIException;
import com.app.blog.payload.JWTAuthRequest;
import com.app.blog.payload.JWTAuthResoonse;
import com.app.blog.payload.UserDTO;
import com.app.blog.security.JWTTokenHelper;
import com.app.blog.service.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/auth/")
@Slf4j
public class AuthController {
	@Autowired
	private JWTTokenHelper jwtTokenHelper;
	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserService userService;

	@PostMapping("/login")
	public ResponseEntity<JWTAuthResoonse> createToken(@RequestBody JWTAuthRequest jwtAuthRequest) {
		log.info("inside controller");
		this.authenticate(jwtAuthRequest.getUsername(), jwtAuthRequest.getPassword());

		UserDetails userDetails = this.userDetailsService.loadUserByUsername(jwtAuthRequest.getUsername());

		String token = this.jwtTokenHelper.generateToken(userDetails);

		JWTAuthResoonse jwtAuthResoonse = new JWTAuthResoonse();
		jwtAuthResoonse.setToken(token);

		return new ResponseEntity<JWTAuthResoonse>(jwtAuthResoonse, HttpStatus.OK);

	}

	private void authenticate(String username, String password) {
        log.info("username and password==:{}",username+"\t"+password);
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
				password);
		try {
			this.authenticationManager.authenticate(authenticationToken);
		} catch (BadCredentialsException e) {
			System.out.println("Invalid credentials");
			throw new APIException("Invalid username and password !!");
		}
		

	}
	
	@PostMapping("/Register")
	public ResponseEntity<UserDTO> registerUser(@RequestBody UserDTO userDTO) {
		UserDTO resigsterUserDTO = this.userService.resigsterNewUser(userDTO);

		return new ResponseEntity<UserDTO>(resigsterUserDTO, HttpStatus.CREATED);

	}
}
