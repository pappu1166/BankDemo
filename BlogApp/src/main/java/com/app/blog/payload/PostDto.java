package com.app.blog.payload;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.app.blog.entities.Category;
import com.app.blog.entities.Comment;
import com.app.blog.entities.User;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Data
public class PostDto {
	private Integer postid;
	private String title;
	private String content;
	private String imageName;

	private Date addeDate;

	private CategoryDto category;

	private UserDTO user;
	
	private Set<CommentDto> comments= new HashSet<>();
}
