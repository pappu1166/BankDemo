package com.app.blog.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.aspectj.weaver.patterns.ThisOrTargetAnnotationPointcut;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.blog.exceptions.ResourceNotFoundException;
import com.app.blog.config.AppConstant;
import com.app.blog.entities.Role;
import com.app.blog.entities.User;
import com.app.blog.payload.UserDTO;
import com.app.blog.reposistories.RoleRepo;
import com.app.blog.reposistories.UserRepo;
import com.app.blog.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private RoleRepo roleRepo;

	@Override
	public UserDTO createUser(UserDTO userDto) {
		User user = this.dtoTouser(userDto);
		User saveUser = this.userRepo.save(user);
		return this.UserToDto(saveUser);
	}

	@Override
	public UserDTO updateUser(UserDTO userDTO, Integer userId) {

		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("user", "Id", userId));

		user.setId(userDTO.getId());
		user.setName(userDTO.getName());
		user.setAbout(userDTO.getAbout());
		user.setPassword(userDTO.getPassword());

		User updateUser = this.userRepo.save(user);
		UserDTO userdto1 = this.UserToDto(updateUser);

		return userdto1;
	}

	@Override
	public UserDTO getUserById(Integer userid) {
		User user = this.userRepo.findById(userid)
				.orElseThrow(() -> new ResourceNotFoundException("user", "Id", userid));
		

		return this.UserToDto(user);
	}

	@Override
	public List<UserDTO> getAllUsers() {
	List<User> users=this.userRepo.findAll();
	List<UserDTO> usersDtos	=users.stream().map(user -> this.UserToDto(user)).collect(Collectors.toList());
	System.out.println("List of user----"+usersDtos);
		return usersDtos;
	}

	@Override
	public void deleteUser(Integer userid) {
	User user=	this.userRepo.findById(userid).orElseThrow(()-> new ResourceNotFoundException("User","Id",userid));
	  this.userRepo.delete(user);  
	
	}

	private User dtoTouser(UserDTO userdto) {
		User user= modelMapper.map(userdto, User.class);
		
		// convert userdto to user class
		//User user = new User();
//		user.setId(userdto.getId());
//		user.setName(userdto.getName());
//		user.setEmail(userdto.getEmail());
//		user.setAbout(userdto.getAbout());
//		user.setPassword(userdto.getPassword());

		return user;

	}

	private UserDTO UserToDto(User user) {
		
		UserDTO dto = modelMapper.map(user, UserDTO.class);
//		UserDTO dto = new UserDTO();
//		dto.setId(user.getId());
//		dto.setName(user.getName());
//		dto.setPassword(user.getPassword());
//		dto.setEmail(user.getEmail());
//		dto.setAbout(user.getAbout());

		return dto;

	}
	
	public UserDTO resigsterNewUser(UserDTO userDTO) {

		User user = this.modelMapper.map(userDTO, User.class);
		// encoded the password
		user.setPassword(this.passwordEncoder.encode(user.getPassword()));

		Role role = this.roleRepo.findById(AppConstant.NORMAL_USER).get();

		user.getRoles().add(role);

		User newuser = this.userRepo.save(user);
		

		return this.modelMapper.map(newuser,UserDTO.class);

	}
	
}
