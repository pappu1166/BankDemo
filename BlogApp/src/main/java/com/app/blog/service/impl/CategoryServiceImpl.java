package com.app.blog.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.blog.entities.Category;
import com.app.blog.entities.User;
import com.app.blog.exceptions.ResourceNotFoundException;
import com.app.blog.payload.CategoryDto;
import com.app.blog.payload.UserDTO;
import com.app.blog.reposistories.CategoryRepo;
import com.app.blog.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepo categoryRepo;
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public CategoryDto createCategory(CategoryDto categoryDto) {
		Category category = this.modelMapper.map(categoryDto, Category.class);
		Category addCategory = this.categoryRepo.save(category);
		return this.modelMapper.map(addCategory, CategoryDto.class);
	}

	@Override
	public CategoryDto updateCategory(CategoryDto categoryDto, Integer categoryid) {

		Category category = this.categoryRepo.findById(categoryid)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "Category Id", categoryid));

		category.setCategoryTitle(categoryDto.getCategoryTitle());
		category.setCategoryDiscription(categoryDto.getCategoryDiscription());
		Category updateCategory = this.categoryRepo.save(category);

		return this.modelMapper.map(updateCategory, CategoryDto.class);
	}

//	@Override
//	public CategoryDto getCategory(Integer categoryid) {
//		Category cate = this.categoryRepo.findById(categoryid)
//				.orElseThrow(() -> new ResourceNotFoundException("Category", " categoryid Id", categoryid));
//
//		return this.modelMapper.map(cate, CategoryDto.class);
//
//	}
	@Override
	public CategoryDto getCategory(Integer categoryId) {
	    // Retrieve category by ID and handle the case if not found
	    Category category = this.categoryRepo.findById(categoryId)
	            .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

	    // Map the found Category entity to CategoryDto
	    return this.modelMapper.map(category, CategoryDto.class);
	}


	@Override

	public List<CategoryDto> getAllCategories() {
		List<Category> categories = this.categoryRepo.findAll();
		System.out.println("all categories====" + categories.toString());

		List<CategoryDto> categoryDtoList = categories.stream()
				.map(cate -> this.modelMapper.map(cate, CategoryDto.class)) // Mapping each individual Category to
																			// CategoryDto
				.collect(Collectors.toList());

		System.out.println("all categoryDto====" + categoryDtoList.toString());

		return categoryDtoList;
	}

	@Override
	public void deleteCategory(Integer categoryid) {
		Category cate = this.categoryRepo.findById(categoryid)
				.orElseThrow(() -> new ResourceNotFoundException("Category", " categoryid Id", categoryid));
		this.categoryRepo.delete(cate);

	}

}
