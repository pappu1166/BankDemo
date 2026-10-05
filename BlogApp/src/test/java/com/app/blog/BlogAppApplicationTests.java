package com.app.blog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.app.blog.reposistories.UserRepo;

@SpringBootTest
class BlogAppApplicationTests {
	
	@Autowired
	private UserRepo userRepo;

	@Test
	void contextLoads() {
	}
	
	@Test
	public void repoTest() {
	String nameClass=	this.userRepo.getClass().getName();
	String packageName= this.userRepo.getClass().getPackageName();
	
	System.out.println("Class name--"+"\t"+nameClass);
	
	System.out.println("packageName--"+"\t"+packageName);
		
	}

}
