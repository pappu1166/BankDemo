package com.app.blog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.blog.payload.ApiResponse;
import com.app.blog.payload.CommentDto;
import com.app.blog.reposistories.CommentRepo;
import com.app.blog.service.CommentService;

@RestController
@RequestMapping("/Api/")
public class CommentController {

	@Autowired
	CommentService commentService;
	@Autowired
	CommentRepo commentRepo;

	@PostMapping("post/comment/{postId}")
	public ResponseEntity<CommentDto> postComments(@RequestBody CommentDto commentDto, @PathVariable int postId) {

		CommentDto comment = commentService.createComment(commentDto, postId);

		return new ResponseEntity<CommentDto>(comment, HttpStatus.CREATED);

	}

	@DeleteMapping("deletecomment/{commentId}")
	public ResponseEntity<ApiResponse> deleteComments(@PathVariable int commentId) {

		commentService.deleteComment(commentId);

		return new ResponseEntity<ApiResponse>(new ApiResponse("Comment deleted sucessfully", "Success"),
				HttpStatus.OK);

	}

}
