package com.gyp.eventservice.repositories.custom;

import java.util.List;

import com.gyp.common.dtos.pagination.PaginatedDto;
import com.gyp.eventservice.entities.CategoryEntity;
import com.gyp.eventservice.services.criteria.CategorySearchCriteria;

public interface CategoryRepositoryCustom {
	List<CategoryEntity> getCategories(CategorySearchCriteria criteria, PaginatedDto paginatedDto);
}
