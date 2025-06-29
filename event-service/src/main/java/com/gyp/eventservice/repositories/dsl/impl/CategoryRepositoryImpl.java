package com.gyp.eventservice.repositories.dsl.impl;

import java.util.List;

import jakarta.persistence.EntityManager;

import com.gyp.common.dtos.pagination.PaginatedDto;
import com.gyp.eventservice.entities.CategoryEntity;
import com.gyp.eventservice.entities.QCategoryEntity;
import com.gyp.eventservice.repositories.dsl.CategoryRepositoryCustom;
import com.gyp.eventservice.services.criteria.CategorySearchCriteria;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class CategoryRepositoryImpl implements CategoryRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	public CategoryRepositoryImpl(EntityManager em) {
		queryFactory = new JPAQueryFactory(em);
	}

	@Override
	public List<CategoryEntity> getCategories(CategorySearchCriteria criteria, PaginatedDto paginatedDto) {
		QCategoryEntity category = QCategoryEntity.categoryEntity;
		BooleanBuilder builder = new BooleanBuilder();

		if(criteria.getId() != null) {
			builder.and(category.id.contains(criteria.getId()));
		}

		if(criteria.getName() != null) {
			builder.and(category.name.lower().contains(criteria.getName().toLowerCase()));
		}

		if(criteria.getDescription() != null) {
			builder.and(category.description.lower().contains(criteria.getDescription().toLowerCase()));
		}

		var result = queryFactory.selectFrom(category).where(builder);

		if(paginatedDto != null) {
			int page = paginatedDto.getPage() > 0 ? paginatedDto.getPage() - 1 : 0;
			int size = paginatedDto.getSize();

			result = result.offset((long)page * size).limit(size);
		}

		return result.fetch();
	}
}
