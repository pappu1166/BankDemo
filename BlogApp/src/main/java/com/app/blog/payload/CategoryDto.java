package com.app.blog.payload;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@Data
public class CategoryDto {

	private Integer categoryId;

	@NotEmpty
	@Size(min=3,message = "size should be more than 3")
	private String categoryTitle;

	@NotEmpty
	@Size(max=10,message = "Max size sould be 10")
	private String categoryDiscription;
}
