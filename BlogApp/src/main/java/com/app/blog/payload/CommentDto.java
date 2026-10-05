package com.app.blog.payload;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class CommentDto {

	private int Id;

	private String content;
}
