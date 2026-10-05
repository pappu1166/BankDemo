package com.app.blog.service;

import com.app.blog.payload.CommentDto;

public interface CommentService {

	CommentDto createComment(CommentDto commentDto, Integer postid);
	
	void deleteComment(Integer commentid);
	
}
