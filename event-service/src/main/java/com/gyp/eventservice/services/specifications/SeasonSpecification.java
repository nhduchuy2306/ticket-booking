package com.gyp.eventservice.services.specifications;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.criteria.Predicate;

import com.gyp.eventservice.entities.SeasonEntity;
import com.gyp.eventservice.services.criteria.SeasonSearchCriteria;
import org.springframework.data.jpa.domain.Specification;

public final class SeasonSpecification {
	private SeasonSpecification() {
	}

	public static Specification<SeasonEntity> createSearchSeasonSpecification(SeasonSearchCriteria criteria) {
		return (root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();

			if(criteria.getId() != null && !criteria.getId().isEmpty()) {
				predicates.add(criteriaBuilder.equal(root.get("id"), criteria.getId()));
			}

			if(criteria.getName() != null && !criteria.getName().isEmpty()) {
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
						"%" + criteria.getName().toLowerCase() + "%"));
			}

			if(criteria.getDescription() != null && !criteria.getDescription().isEmpty()) {
				predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("description")),
						"%" + criteria.getDescription().toLowerCase() + "%"));
			}

			if(criteria.getOrganizationId() != null && !criteria.getOrganizationId().isEmpty()) {
				predicates.add(criteriaBuilder.equal(root.get("organizationId"), criteria.getOrganizationId()));
			}

			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		};
	}
}
