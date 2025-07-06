package com.gyp.eventservice.services;

import java.util.List;

import com.gyp.common.dtos.pagination.PaginatedDto;
import com.gyp.common.exceptions.ResourceNotFoundException;
import com.gyp.common.intefaces.Validatable;
import com.gyp.eventservice.dtos.category.CategoryRequestDto;
import com.gyp.eventservice.dtos.category.CategoryResponseDto;
import com.gyp.eventservice.services.criteria.CategorySearchCriteria;

public interface CategoryService extends Validatable {
	CategoryResponseDto createCategory(CategoryRequestDto categoryRequestDto);

	List<CategoryResponseDto> getAllCategories(CategorySearchCriteria criteria, PaginatedDto pagination);

	CategoryResponseDto getCategoryById(String categoryId) throws ResourceNotFoundException;

	void deleteCategory(String categoryId) throws ResourceNotFoundException;
}
