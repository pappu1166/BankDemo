package com.app.blog.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.blog.entities.Comment;
import com.app.blog.entities.Post;
import com.app.blog.exceptions.ResourceNotFoundException;
import com.app.blog.payload.CommentDto;
import com.app.blog.reposistories.CommentRepo;
import com.app.blog.reposistories.PostRepo;
import com.app.blog.service.CommentService;

@Service
public class CommentSeviceImpl implements CommentService {

	@Autowired
	private PostRepo postRepo;
	@Autowired
	private CommentRepo commentRepo;
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public CommentDto createComment(CommentDto commentDto, Integer postid) {

		Post post = this.postRepo.findById(postid)
				.orElseThrow(() -> new ResourceNotFoundException("Post", "post id", postid));

		Comment comment = this.modelMapper.map(commentDto, Comment.class);
		comment.setPost(post);

		Comment saveComment = this.commentRepo.save(comment);

		return this.modelMapper.map(saveComment, CommentDto.class);
	}

	@Override
	public void deleteComment(Integer commentid) {

		Comment comment = this.commentRepo.findById(commentid)
				.orElseThrow(() -> new ResourceNotFoundException("Comment", "comment id", commentid));

		this.commentRepo.delete(comment);

	}

}
