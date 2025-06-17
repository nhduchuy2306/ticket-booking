package com.gyp.eventservice.services;

import java.util.List;

import com.gyp.eventservice.dtos.category.CategoryRequestDto;
import com.gyp.eventservice.dtos.category.CategoryResponseDto;
import com.gyp.eventservice.exceptions.CategoryNotFoundException;

public interface CategoryService {
	CategoryResponseDto createCategory(CategoryRequestDto request);

	List<CategoryResponseDto> getAllCategories();

	List<CategoryResponseDto> getCategoriesByEvent(String eventId);

	void assignCategoriesToEvent(String eventId, List<String> categoryIds);

	CategoryResponseDto getCategoryById(String categoryId) throws CategoryNotFoundException;
}
