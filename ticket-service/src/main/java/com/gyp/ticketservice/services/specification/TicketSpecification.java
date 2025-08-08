package com.gyp.ticketservice.services.specification;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.criteria.Predicate;

import com.gyp.ticketservice.entities.TicketEntity;
import com.gyp.ticketservice.services.criteria.TicketSearchCriteria;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

public final class TicketSpecification {
	private TicketSpecification() {
	}

	private static final String EVENT_ID = "eventId";
	private static final String TICKET_ID = "ticketId";
	private static final String STATUS = "status";
	private static final String TICKET_TYPE = "ticketType";
	private static final String SEAT_NUMBER = "seatNumber";
	private static final String ORDER_BY = "orderBy";

	public static Specification<TicketEntity> createTicketSpecification(TicketSearchCriteria criteria) {
		return (root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();
			if(StringUtils.isNotEmpty(criteria.getEventId())) {
				predicates.add(criteriaBuilder.equal(root.get(EVENT_ID), criteria.getEventId()));
			}
			if(StringUtils.isNotEmpty(criteria.getTicketId())) {
				predicates.add(criteriaBuilder.equal(root.get(TICKET_ID), criteria.getTicketId()));
			}
			if(StringUtils.isNotEmpty(criteria.getOrganizationId())) {
				predicates.add(criteriaBuilder.equal(root.get("organizationId"), criteria.getOrganizationId()));
			}
			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		};
	}
}
