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
import org.springframework.web.bind.annotation.RestController;

import com.app.blog.payload.ApiResponse;
import com.app.blog.payload.CategoryDto;
import com.app.blog.service.CategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	// create Category
	@PostMapping("/")
	public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
		System.out.println("inside create category");
		CategoryDto createdUserDTO = this.categoryService.createCategory(categoryDto);
		return new ResponseEntity<>(createdUserDTO, HttpStatus.CREATED);
	}

	// Update Category
	@PutMapping("/update/{categoryId}")
	public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categoryDto,
			@PathVariable("categoryId") Integer uId) {

		CategoryDto updateCategoryDto = this.categoryService.updateCategory(categoryDto, uId);
		return ResponseEntity.ok(updateCategoryDto);
	}

	// Delete Category

	@DeleteMapping("/delete/{categoryId}")
	public ResponseEntity<ApiResponse> deleteCategory(@PathVariable("categoryId") Integer uId) {

		this.categoryService.deleteCategory(uId);
		return new ResponseEntity<ApiResponse>(new ApiResponse("Category deleted sucessfully", "Success"),
				HttpStatus.OK);
	}

	// getAll Category

	@GetMapping("/getAllCategory")
	public ResponseEntity<List<CategoryDto>> getAllCategory() {

		return ResponseEntity.ok(this.categoryService.getAllCategories());
	}

	@GetMapping("/getcategory/{categoryId}")
	public ResponseEntity<CategoryDto> getCategory(@PathVariable Integer categoryId) {
	System.out.println("inside get category");
		CategoryDto categoryDto = this.categoryService.getCategory(categoryId);

		
		if (categoryDto == null) {
			return ResponseEntity.notFound().build();
		}

		// Return the category details with a 200 OK response
		return ResponseEntity.ok(categoryDto);
	}

}
