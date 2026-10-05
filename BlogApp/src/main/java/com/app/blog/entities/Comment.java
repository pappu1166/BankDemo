package com.app.blog.entities;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Getter
@Setter
@NoArgsConstructor
@Data
@Table(
	    name = "Comments",
	    indexes = {
	        @Index(name = "idx_comment_id", columnList = "Id"),
	        @Index(name = "idx_comment_id_content", columnList = "content"),
	        @Index(name = "idx_comment_post", columnList = "post_postid")
	    }
	)
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int Id;
	
	private String content;
	
	@ManyToOne
	private Post post;
}
