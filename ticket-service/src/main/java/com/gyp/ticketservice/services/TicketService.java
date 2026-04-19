package com.gyp.ticketservice.services;

import java.util.List;

import com.gyp.common.dtos.pagination.PaginatedDto;
import com.gyp.ticketservice.dtos.ticket.TicketResponseDto;
import com.gyp.ticketservice.services.criteria.TicketSearchCriteria;

public interface TicketService {
	TicketResponseDto getTicketById(String id);

	List<TicketResponseDto> getTicketByEventId(String eventId);

	List<TicketResponseDto> getAvailableTicketsByEventId(String eventId, String organizationId);

	List<TicketResponseDto> getAllTickets(TicketSearchCriteria criteria, PaginatedDto pagination);

	void startSaleTicket(String eventId);
}
