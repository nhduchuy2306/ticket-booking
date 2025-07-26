package com.gyp.authservice.services.specifications;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;

import com.gyp.authservice.entities.UserGroupEntity;
import com.gyp.authservice.services.criteria.UserGroupSearchCriteria;
import com.gyp.common.constants.AppConstants;
import com.gyp.common.utils.SortUtils;
import org.springframework.data.jpa.domain.Specification;

public final class UserGroupSpecification {
	public static Specification<UserGroupEntity> createSearchUserGroupSpecification(UserGroupSearchCriteria criteria) {
		return (root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();

			// Organization ID filter
			if(criteria.getOrganizationId() != null && !criteria.getOrganizationId().trim().isEmpty()) {
				predicates.add(
						criteriaBuilder.equal(root.get("organizationEntity").get("id"), criteria.getOrganizationId()));
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
