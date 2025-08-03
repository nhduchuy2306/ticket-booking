package com.gyp.eventservice.services.specifications;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.criteria.Predicate;

import com.gyp.eventservice.entities.VenueMapEntity;
import com.gyp.eventservice.services.criteria.VenueMapSearchCriteria;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

public final class VenueMapSpecification {
	private VenueMapSpecification() {
	}

	public static Specification<VenueMapEntity> createVenueMapSpecification(VenueMapSearchCriteria criteria) {
		return (root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();

			if(StringUtils.isNotEmpty(criteria.getOrganizationId())) {
				predicates.add(criteriaBuilder.equal(root.get("organizationId"), criteria.getOrganizationId()));
			}
			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		};
	}
}
