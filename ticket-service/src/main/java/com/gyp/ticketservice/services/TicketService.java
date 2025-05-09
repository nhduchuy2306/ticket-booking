package com.gyp.ticketservice.services;

import com.gyp.ticketservice.dtos.ticket.TicketResponseDto;

public interface TicketService {
	TicketResponseDto getTicketById(String id);

}
