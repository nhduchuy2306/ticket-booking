package com.gyp.eventservice.services.impl;

import java.util.List;
import java.util.Optional;

import com.gyp.common.dtos.pagination.PaginatedDto;
import com.gyp.common.exceptions.ResourceDuplicateException;
import com.gyp.common.exceptions.ResourceNotFoundException;
import com.gyp.common.services.ValidationService;
import com.gyp.common.validators.criteria.ValidationInfo;
import com.gyp.eventservice.dtos.category.CategoryRequestDto;
import com.gyp.eventservice.dtos.category.CategoryResponseDto;
import com.gyp.eventservice.entities.CategoryEntity;
import com.gyp.eventservice.mappers.CategoryMapper;
import com.gyp.eventservice.repositories.CategoryRepository;
import com.gyp.eventservice.services.CategoryService;
import com.gyp.eventservice.services.criteria.CategorySearchCriteria;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
	private final CategoryRepository categoryRepository;
	private final ValidationService validationService;
	private final CategoryMapper categoryMapper;

	@Override
	public CategoryResponseDto createCategory(CategoryRequestDto categoryRequestDto) {
		Optional<CategoryEntity> categoryEntity = categoryRepository.findByName(categoryRequestDto.getName());
		if(categoryEntity.isPresent()) {
			throw new ResourceDuplicateException(
					String.format("Category with name %s already exists", categoryRequestDto.getName()));
		}
		CategoryEntity newCategoryEntity = new CategoryEntity();
		categoryMapper.updateEntityFromDto(categoryRequestDto, newCategoryEntity);

		CategoryEntity savedCategoryEntity = categoryRepository.save(newCategoryEntity);
		return categoryMapper.toResponseDto(savedCategoryEntity);
	}

	@Override
	public List<CategoryResponseDto> getAllCategories(CategorySearchCriteria criteria, PaginatedDto pagination) {
		if(criteria == null) {
			criteria = new CategorySearchCriteria();
		}
		if(pagination == null) {
			pagination = new PaginatedDto();
		}

		List<CategoryEntity> categories = categoryRepository.getCategories(criteria, pagination);
		return categoryMapper.toResponseDtoList(categories);
	}

	@Override
	public CategoryResponseDto getCategoryById(String categoryId) throws ResourceNotFoundException {
		Optional<CategoryEntity> categoryEntity = categoryRepository.findById(categoryId);
		return categoryEntity.map(categoryMapper::toResponseDto).orElse(null);
	}

	@Override
	public CategoryResponseDto updateCategory(String categoryId, CategoryRequestDto categoryRequestDto)
			throws ResourceNotFoundException {
		if(categoryId == null || categoryId.isEmpty()) {
			throw new ResourceNotFoundException("Category ID must not be null or empty");
		}

		Optional<CategoryEntity> existingCategory = categoryRepository.findById(categoryId);
		if(existingCategory.isEmpty()) {
			throw new ResourceNotFoundException("Category with ID " + categoryId + " not found");
		}

		Optional<CategoryEntity> duplicateCategory = categoryRepository.findByName(categoryRequestDto.getName());
		if(duplicateCategory.isPresent() && !duplicateCategory.get().getId().equals(categoryId)) {
			throw new ResourceDuplicateException(
					String.format("Category with name %s already exists", categoryRequestDto.getName()));
		}

		CategoryEntity updatedCategoryEntity = existingCategory.get();
		categoryMapper.updateEntityFromDto(categoryRequestDto, updatedCategoryEntity);
		CategoryEntity savedCategoryEntity = categoryRepository.save(updatedCategoryEntity);
		log.info("Updated category with ID: {}", categoryId);
		return categoryMapper.toResponseDto(savedCategoryEntity);
	}

	@Override
	public void deleteCategory(String categoryId) throws ResourceNotFoundException {
		if(categoryId == null || categoryId.isEmpty()) {
			throw new ResourceNotFoundException("Category ID must not be null or empty");
		}
		Optional<CategoryEntity> categoryEntity = categoryRepository.findById(categoryId);
		if(categoryEntity.isEmpty()) {
			throw new ResourceNotFoundException("Category with ID " + categoryId + " not found");
		}
		categoryRepository.delete(categoryEntity.get());
		log.info("Deleted category with ID: {}", categoryId);
	}

	@Override
	public ValidationInfo validate(Class<?> clazz) {
		var validationInfo = validationService.extractValidationInfo(clazz);
		if(validationInfo == null) {
			log.warn("No validation info found for request class: {}", clazz.getName());
			return ValidationInfo.empty();
		}
		return validationInfo;
	}
}
