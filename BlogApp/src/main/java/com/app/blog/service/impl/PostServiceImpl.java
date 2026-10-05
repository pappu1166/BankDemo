package com.app.blog.service.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import com.app.blog.entities.Category;
import com.app.blog.entities.Post;
import com.app.blog.entities.User;
import com.app.blog.exceptions.ResourceNotFoundException;
import com.app.blog.payload.PostDto;
import com.app.blog.payload.PostResponse;
import com.app.blog.reposistories.CategoryRepo;
import com.app.blog.reposistories.PostRepo;
import com.app.blog.reposistories.UserRepo;
import com.app.blog.service.PostService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PostServiceImpl implements PostService {

	@Autowired
	PostRepo postRepo;
	@Autowired
	UserRepo userRepo;
	@Autowired
	CategoryRepo categoryRepo;
	@Autowired
	ModelMapper modelMapper;
	
	//private static final Logger logger=LoggerFactory.getLogger(PostServiceImpl.class);

	@Override
	public PostDto CreatePost(PostDto postDto, Integer userId, Integer categoryId) {
		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("user", "Id", userId));

		Category category = this.categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

		Post post = this.modelMapper.map(postDto, Post.class);
		post.setImageName("default.png");
		post.setAddeDate(new Date());
		post.setUser(user);
		post.setCategory(category);

		Post newpost = this.postRepo.save(post);

		return this.modelMapper.map(newpost, PostDto.class);
	}

	@Override
	public PostDto updatePost(PostDto postDto, Integer postid) {
		Post post = this.postRepo.findById(postid)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "Post id", postid));

		post.setTitle(postDto.getTitle());
		post.setContant(postDto.getContent());
		post.setImageName(postDto.getImageName());

		Post updatePost = this.postRepo.save(post);

		return this.modelMapper.map(updatePost, PostDto.class);
	}

	@Override
	public void deletePost(Integer postId) {
		Post post = this.postRepo.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "Post id", postId));
		this.postRepo.delete(post);

	}

//	@Override
//	public List<PostDto> getAllPost(Integer pagenumber, Integer pagesize) {
//
//		Pageable page = PageRequest.of(pagenumber, pagesize);
//
//		Page<Post> pagepost = this.postRepo.findAll(page);
//		List<Post> postall = pagepost.getContent();
//
//		System.out.println("allpost->>>>>>>>>"+postall.toString());
//		List<PostDto> postdto = postall.stream().map((post) -> this.modelMapper.map(post, PostDto.class))
//				.collect(Collectors.toList());
//		return postdto;
//	}
	
	@Override
	public PostResponse getAllPost(Integer pageNumber, Integer pageSize,String sortBy,String direction) {

	    // Validate the inputs
	    int page = pageNumber;//(pageNumber != null && pageNumber >= 0) ? pageNumber : 0;
	    int size = pageSize;//(pageSize != null && pageSize > 0) ? pageSize :10;  // Default to 10 if invalid
	    Sort directSort=null;
	    if(direction.equalsIgnoreCase("asc")) {
	    	directSort=Sort.by(sortBy).ascending();
	    }else {
	    	directSort=Sort.by(sortBy).descending();
	    }
	    log.info("pagenumber  pageable->>>>: {}", pageNumber);
	    log.info("page size pageable->>>>: {}",pageSize);
	    // Create pageable object
	    Pageable pageable = PageRequest.of(page, size,directSort);
	    log.info("Fetched pageable->>>>: {}", pageable);
	    // Fetch posts from the repository
	    Page<Post> pagePost = this.postRepo.findAll(pageable);
	    List<Post> postAll = pagePost.getContent();

	  //  logger.info("Fetched Posts: {}", postAll);
	    log.info("Fetched Posts: {}", postAll);

	    // Map Post entities to PostDto
	    List<PostDto> postdtos = postAll.stream()
	                  .map(post -> this.modelMapper.map(post, PostDto.class))
	                  .toList(); // Cleaner with Java 16+ (or continue using collect(Collectors.toList()))
	    
	    PostResponse postResponse= new PostResponse();
	    postResponse.setContains(postdtos);
	    postResponse.setPageNumber(pagePost.getNumber());
	    postResponse.setPageSize(pagePost.getSize());
	    postResponse.setTotalElement(pagePost.getTotalElements());
	    postResponse.setTotalPage(pagePost.getTotalPages());
	    postResponse.setLastPage(pagePost.isLast());
	    log.info("Fetched all **********postResponse***********: {}", postResponse);
		return postResponse;
	    
	    
	    
	}

	@Override
	public PostDto getPostById(Integer postId) {
		Post post = this.postRepo.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "Post Id", postId));
		 log.info("Inside getPostById****: {}");
		return this.modelMapper.map(post, PostDto.class);
	}

	@Override
	public List<PostDto> getPostsByCategory(Integer categoryId) {
		Category cat = this.categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "category id", categoryId));

		List<Post> posts = this.postRepo.findByCategory(cat);

		List<PostDto> postsdto = posts.stream().map((post) -> this.modelMapper.map(post, PostDto.class))
				.collect(Collectors.toList());

		return postsdto;
	}

	@Override
	public List<PostDto> getPostsByUser(Integer userId) {

		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "User id", userId));

		System.out.println("user===" + user.toString());
		List<Post> posts = this.postRepo.findByUser(user);
		List<PostDto> postsdto = posts.stream().map((post) -> this.modelMapper.map(post, PostDto.class))
				.collect(Collectors.toList());

		System.out.println("get all post by user===" + postsdto.toString());

		return postsdto;
	}

	@Override
	public List<PostDto> searchPost(String keyword) {
		List<Post> titleList=	this.postRepo.findByTitleContaining(keyword);
		List<PostDto> postsdto = titleList.stream().map((post) -> this.modelMapper.map(post, PostDto.class))
				.collect(Collectors.toList());
		return postsdto;
	}

}
