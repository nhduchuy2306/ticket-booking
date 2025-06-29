package com.gyp.eventservice.services.specifications;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;

import com.gyp.common.constants.AppConstants;
import com.gyp.common.utils.PropertyName;
import com.gyp.common.utils.SortUtils;
import com.gyp.eventservice.entities.CategoryEntity;
import com.gyp.eventservice.services.criteria.CategorySearchCriteria;
import org.springframework.data.jpa.domain.Specification;

public final class CategorySpecification {
	public static Specification<CategoryEntity> createSearchCategorySpecification(CategorySearchCriteria criteria) {
		return (root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();

			// ID filter
			if(criteria.getId() != null && !criteria.getId().trim().isEmpty()) {
				predicates.add(
						criteriaBuilder.equal(root.get(PropertyName.of(CategoryEntity::getId)),
								criteria.getId()));
			}

			// Name filter
			if(criteria.getName() != null && !criteria.getName().trim().isEmpty()) {
				String namePattern = "%" + criteria.getName().toLowerCase() + "%";
				predicates.add(
						criteriaBuilder.like(
								criteriaBuilder.lower(root.get(PropertyName.of(CategoryEntity::getName))),
								namePattern));
			}

			// Description filter
			if(criteria.getDescription() != null && !criteria.getDescription().trim().isEmpty()) {
				String descriptionPattern = "%" + criteria.getDescription().toLowerCase() + "%";
				predicates.add(
						criteriaBuilder.like(criteriaBuilder.lower(
										root.get(PropertyName.of(CategoryEntity::getDescription))),
								descriptionPattern));
			}

			// Sort by
			if(query != null && criteria.getSortBy() != null && !criteria.getSortBy().trim().isEmpty()) {
				String[] sortByParts = criteria.getSortBy().split(AppConstants.COMMA);
				List<Order> orders = new ArrayList<>();
				var listSortFields = SortUtils.getSortFields(sortByParts);

				for(var sortField : listSortFields) {
					String fieldName = sortField.getLeft();
					Boolean isAscending = sortField.getRight();

					if(!fieldName.isEmpty()) {
						if(isAscending) {
							orders.add(criteriaBuilder.asc(root.get(fieldName)));
						} else {
							orders.add(criteriaBuilder.desc(root.get(fieldName)));
						}
					}
				}
				query.orderBy(orders);
			}

			// Combine all predicates
			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		};
	}
}
