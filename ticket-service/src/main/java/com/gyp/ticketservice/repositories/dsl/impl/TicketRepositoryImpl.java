package com.gyp.ticketservice.repositories.dsl.impl;

import java.util.List;

import jakarta.persistence.EntityManager;

import com.gyp.common.enums.event.TicketStatus;
import com.gyp.ticketservice.entities.QTicketEntity;
import com.gyp.ticketservice.entities.TicketEntity;
import com.gyp.ticketservice.repositories.dsl.TicketRepositoryCustom;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class TicketRepositoryImpl implements TicketRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	public TicketRepositoryImpl(EntityManager em) {
		queryFactory = new JPAQueryFactory(em);
	}

	@Override
	public List<TicketEntity> findTicketsByDynamicFilters(String eventId, String status, String attendeeEmail) {
		QTicketEntity ticket = QTicketEntity.ticketEntity;

		BooleanBuilder builder = new BooleanBuilder();

		if(eventId != null) {
			builder.and(ticket.eventId.eq(eventId));
		}
		if(status != null) {
			builder.and(ticket.status.eq(TicketStatus.valueOf(status)));
		}
		if(attendeeEmail != null) {
			builder.and(ticket.attendeeEmail.containsIgnoreCase(attendeeEmail));
		}

		return queryFactory.selectFrom(ticket).where(builder).fetch();
	}

	@Override
	public TicketEntity findTicketById(String id) {
		QTicketEntity ticket = QTicketEntity.ticketEntity;
		BooleanBuilder builder = new BooleanBuilder();

		if(id != null) {
			builder.and(ticket.id.eq(id));
		}

		return queryFactory.selectFrom(QTicketEntity.ticketEntity).where(builder).fetchFirst();
	}
}
