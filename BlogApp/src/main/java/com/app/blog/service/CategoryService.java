package com.app.blog.service;

import java.util.List;

import com.app.blog.entities.Category;
import com.app.blog.payload.CategoryDto;


public interface CategoryService {

	CategoryDto createCategory(CategoryDto categoryDto);

	CategoryDto updateCategory(CategoryDto categoryDto, Integer categoryid);

	CategoryDto getCategory(Integer categoryid);

	List<CategoryDto> getAllCategories();

	void deleteCategory(Integer categoryid);
}
