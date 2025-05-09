package com.gyp.ticketservice.repositories.dsl;

import java.util.List;

import com.gyp.ticketservice.entities.TicketEntity;

public interface TicketRepositoryCustom {
	List<TicketEntity> findTicketsByDynamicFilters(String eventId, String status, String attendeeEmail);

	TicketEntity findTicketById(String id);
}
