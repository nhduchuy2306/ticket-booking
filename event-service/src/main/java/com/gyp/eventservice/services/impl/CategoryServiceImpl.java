package com.gyp.eventservice.services.impl;

import java.util.List;
import java.util.Optional;

import com.gyp.common.dtos.pagination.PaginatedDto;
import com.gyp.common.exceptions.DuplicateResourceException;
import com.gyp.common.exceptions.ResourceNotFoundException;
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
	private final CategoryMapper categoryMapper;

	@Override
	public CategoryResponseDto createCategory(CategoryRequestDto categoryRequestDto) {
		Optional<CategoryEntity> categoryEntity = categoryRepository.findByName(categoryRequestDto.getName());
		if(categoryEntity.isPresent()) {
			throw new DuplicateResourceException(
					String.format("Category with name %s already exists", categoryRequestDto.getName()));
		}
		CategoryEntity newCategoryEntity = new CategoryEntity();
		categoryMapper.updateEntityFromDto(categoryRequestDto, newCategoryEntity);

		CategoryEntity savedCategoryEntity = categoryRepository.save(newCategoryEntity);
		return categoryMapper.toResponseDto(savedCategoryEntity);
	}

	@Override
	public List<CategoryResponseDto> getAllCategories(CategorySearchCriteria criteria, PaginatedDto pagination) {
		//		Specification<CategoryEntity> specification = CategorySpecification.createSearchCategorySpecification(criteria);
		//		Pageable pageable = PageRequest.of(
		//				pagination.getPage() > 0 ? pagination.getPage() - 1 : 0,
		//				pagination.getSize());
		//		Page<CategoryEntity> categoryPage = categoryRepository.findAll(specification, pageable);
		//		return categoryPage.hasContent()
		//				? categoryMapper.toResponseDtoList(categoryPage.getContent())
		//				: Collections.emptyList();
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
}
