package com.app.blog.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.blog.config.AppConstant;
import com.app.blog.payload.ApiResponse;
import com.app.blog.payload.PostDto;
import com.app.blog.payload.PostResponse;
import com.app.blog.service.PostService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/")
@Slf4j
public class PostController {
	@Autowired
	private PostService postService;

	@PostMapping("/user/{userId}/category/{categoryId}/posts")
	private ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto, @PathVariable Integer userId,
			@PathVariable Integer categoryId) {

		PostDto createPostDto = this.postService.CreatePost(postDto, userId, categoryId);

		return new ResponseEntity<PostDto>(createPostDto, HttpStatus.CREATED);
	}

//get by user
	@GetMapping("/user/{userId}/posts")
	private ResponseEntity<List<PostDto>> getPostByUser(@PathVariable Integer userId) {

		List<PostDto> posts = this.postService.getPostsByUser(userId);

		return new ResponseEntity<List<PostDto>>(posts, HttpStatus.OK);

	}

//get by category
	@GetMapping("/category/{CategoryId}/posts")
	private ResponseEntity<List<PostDto>> getPostByCategory(@PathVariable Integer CategoryId) {

		List<PostDto> posts = this.postService.getPostsByCategory(CategoryId);

		return new ResponseEntity<List<PostDto>>(posts, HttpStatus.OK);

	}

//get all posts
	@GetMapping("/allPosts")
	public ResponseEntity<PostResponse> getAllPosts(
			@RequestParam(defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(defaultValue = AppConstant.SORT_BY,required = false) String sortBy,
			@RequestParam(defaultValue = AppConstant.SORT_DIRECTION,required = false) String direction) {

		log.info("Page number in controller: {}", pageNumber);
		log.info("Page size in controller: {}", pageSize);

		PostResponse allPosts = this.postService.getAllPost(pageNumber, pageSize,sortBy,direction);

		return new ResponseEntity<PostResponse>(allPosts,HttpStatus.OK);
	}

	// get single post

	@GetMapping("/singlePosts/{postId}")
	private ResponseEntity<PostDto> getAllPosts(@PathVariable Integer postId) {

		PostDto posts = this.postService.getPostById(postId);
		log.info("getall the post for postId: {}", posts);
		return new ResponseEntity<PostDto>(posts, HttpStatus.OK);

	}
//delete post

	@DeleteMapping("/deletePosts/{postId}")
	private ResponseEntity<ApiResponse> deletePost(@PathVariable Integer postId) {

		this.postService.deletePost(postId);

		return new ResponseEntity<ApiResponse>(new ApiResponse("Post deleted sucessfully", "Success"), HttpStatus.OK);

	}

	// update post

	@PutMapping("/updatePosts/{postId}")
	private ResponseEntity<PostDto> updatePosts(@RequestBody PostDto postDto, @PathVariable Integer postId) {

		PostDto updatePostDto = this.postService.updatePost(postDto, postId);

		return new ResponseEntity<PostDto>(updatePostDto, HttpStatus.OK);

	}
	
	@GetMapping("/posts/search/{keywords}")
	private ResponseEntity <List<PostDto>> searchPost(@PathVariable String keywords) {

		List<PostDto> posts = this.postService.searchPost(keywords);

		return new ResponseEntity <List<PostDto>>(posts, HttpStatus.OK);

	}
	
	
}
