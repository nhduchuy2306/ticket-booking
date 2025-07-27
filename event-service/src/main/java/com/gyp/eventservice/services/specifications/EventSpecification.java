package com.gyp.eventservice.services.specifications;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

import com.gyp.common.enums.event.TicketStatus;
import com.gyp.common.utils.PropertyName;
import com.gyp.eventservice.entities.CategoryEntity;
import com.gyp.eventservice.entities.EventEntity;
import com.gyp.eventservice.entities.TicketTypeEntity;
import com.gyp.eventservice.services.criteria.EventSearchCriteria;
import org.springframework.data.jpa.domain.Specification;

public final class EventSpecification {
	private EventSpecification() {
	}

	public static Specification<EventEntity> createSearchEventSpecification(EventSearchCriteria criteria) {
		return (root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();

			// Organization ID filter
			if(criteria.getOrganizationId() != null && !criteria.getOrganizationId().trim().isEmpty()) {
				predicates.add(
						criteriaBuilder.equal(root.get(PropertyName.of(CategoryEntity::getOrganizationId)),
								criteria.getOrganizationId()));
			}

			// Keyword search
			if(criteria.getKeyword() != null && !criteria.getKeyword().trim().isEmpty()) {
				String keyword = "%" + criteria.getKeyword().toLowerCase() + "%";

				Predicate namePredicate = criteriaBuilder.like(
						criteriaBuilder.lower(root.get("name")), keyword);
				Predicate descriptionPredicate = criteriaBuilder.like(
						criteriaBuilder.lower(root.get("description")), keyword);

				predicates.add(criteriaBuilder.or(namePredicate, descriptionPredicate));
			}

			// Organizer filter
			if(criteria.getOrganizerId() != null) {
				predicates.add(criteriaBuilder.equal(
						root.get("organizerEntity").get("id"), criteria.getOrganizerId()));
			}

			// Status filter
			if(criteria.getStatus() != null) {
				predicates.add(criteriaBuilder.equal(root.get("status"), criteria.getStatus()));
			}

			// Date range filters
			if(criteria.getStartDateFrom() != null) {
				predicates.add(criteriaBuilder.greaterThanOrEqualTo(
						root.get("time").get("startTime"), criteria.getStartDateFrom()));
			}

			if(criteria.getStartDateTo() != null) {
				predicates.add(criteriaBuilder.lessThanOrEqualTo(
						root.get("time").get("startTime"), criteria.getStartDateTo()));
			}

			// Location filters
			if(criteria.getCity() != null) {
				predicates.add(criteriaBuilder.equal(
						root.get("venueEntity").get("city"), criteria.getCity()));
			}

			if(criteria.getCountry() != null) {
				predicates.add(criteriaBuilder.equal(
						root.get("venueEntity").get("country"), criteria.getCountry()));
			}

			// Venue capacity filters
			if(criteria.getMinCapacity() != null) {
				predicates.add(criteriaBuilder.greaterThanOrEqualTo(
						root.get("venueEntity").get("capacity"), criteria.getMinCapacity()));
			}

			if(criteria.getMaxCapacity() != null) {
				predicates.add(criteriaBuilder.lessThanOrEqualTo(
						root.get("venueEntity").get("capacity"), criteria.getMaxCapacity()));
			}

			// Category filter
			if(criteria.getCategoryIds() != null && !criteria.getCategoryIds().isEmpty()) {
				predicates.add(root.join("categoryEntityList").get("id").in(criteria.getCategoryIds()));
			}

			// Available tickets filter
			if(criteria.getHasAvailableTickets() != null && criteria.getHasAvailableTickets() && query != null) {
				Subquery<TicketTypeEntity> subquery = query.subquery(TicketTypeEntity.class);
				Root<TicketTypeEntity> subRoot = subquery.from(TicketTypeEntity.class);
				predicates.add(criteriaBuilder.exists(
						subquery.select(subRoot)
								.where(
										criteriaBuilder.and(
												criteriaBuilder.equal(root.get("id"), root.get("id")),
												criteriaBuilder.equal(root.get("status"), TicketStatus.AVAILABLE),
												criteriaBuilder.greaterThan(root.get("quantityAvailable"), 0)
										)
								)
				));
			}

			// Price range filters (requires join with ticket types)
			if(criteria.getMinPrice() != null || criteria.getMaxPrice() != null) {
				var ticketJoin = root.join("ticketTypeEntityList");

				if(criteria.getMinPrice() != null) {
					predicates.add(criteriaBuilder.greaterThanOrEqualTo(
							ticketJoin.get("price"), criteria.getMinPrice()));
				}

				if(criteria.getMaxPrice() != null) {
					predicates.add(criteriaBuilder.lessThanOrEqualTo(
							ticketJoin.get("price"), criteria.getMaxPrice()));
				}
			}

			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		};
	}
}
