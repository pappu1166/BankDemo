package com.app.blog;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.internal.bytebuddy.asm.Advice.This;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.app.blog.config.AppConstant;
import com.app.blog.entities.Role;
import com.app.blog.reposistories.RoleRepo;

@SpringBootApplication
public class BlogAppApplication implements CommandLineRunner{
     @Autowired
	private PasswordEncoder passwordEncoder;
     @Autowired
     private RoleRepo roleRepo;
	
	public static void main(String[] args) {
		SpringApplication.run(BlogAppApplication.class, args);
		System.out.println(" blogger Ap plication start");

	}

    @Bean
    ModelMapper modelMapper() {
		
		return new ModelMapper();
		
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println(this.passwordEncoder.encode("tttt"));
		
		Role role =new Role();
		role.setId(AppConstant.NORMAL_USER);
		role.setName("Normal_User");
		
		Role role1 =new Role();
		role1.setId(AppConstant.ADMIN_USER);
		role1.setName("Admin_User");
		
		List<Role> roles=List.of(role,role1);
	List<Role>	result=this.roleRepo.saveAll(roles);
	
	result.forEach(r-> System.out.println(r.getName()));
		
	}
}
