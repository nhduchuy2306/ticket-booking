package com.gyp.eventservice.services.impl;

import java.util.List;

import com.gyp.eventservice.dtos.category.CategoryRequestDto;
import com.gyp.eventservice.dtos.category.CategoryResponseDto;
import com.gyp.eventservice.repositories.CategoryRepository;
import com.gyp.eventservice.services.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
	private final CategoryRepository categoryRepository;

	@Override
	public CategoryResponseDto createCategory(CategoryRequestDto request) {
		return null;
	}

	@Override
	public List<CategoryResponseDto> getAllCategories() {
		return List.of();
	}

	@Override
	public List<CategoryResponseDto> getCategoriesByEvent(String eventId) {
		return List.of();
	}

	@Override
	public void assignCategoriesToEvent(String eventId, List<String> categoryIds) {

	}

	@Override
	public CategoryResponseDto getCategoryById(String categoryId) {
		return null;
	}
}
