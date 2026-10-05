package com.app.blog.service;

import java.util.List;

import com.app.blog.entities.Post;
import com.app.blog.payload.PostDto;
import com.app.blog.payload.PostResponse;

public interface PostService {

	PostDto CreatePost(PostDto postDto, Integer userId, Integer categoryId);

	PostDto updatePost(PostDto postDto, Integer postid);

	void deletePost(Integer postId);

	PostResponse getAllPost(Integer pagenumber, Integer pagesize,String sortBy,String direction);

	PostDto getPostById(Integer postId);

	List<PostDto> getPostsByCategory(Integer categoryId);

	List<PostDto> getPostsByUser(Integer userId);

	List<PostDto> searchPost(String keyword);
}
