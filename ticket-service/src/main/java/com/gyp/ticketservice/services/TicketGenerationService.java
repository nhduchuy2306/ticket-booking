package com.gyp.ticketservice.services;

import com.gyp.ticketservice.dtos.seatmap.SeatMapDto;
import com.gyp.ticketservice.dtos.ticketgeneration.TicketGenerationResponseDto;
import com.gyp.ticketservice.dtos.ticketgeneration.TicketGenerationSummaryDto;

public interface TicketGenerationService {
	TicketGenerationSummaryDto validateTicket(String ticketNumber);

	TicketGenerationResponseDto getTicketGenerationById(String id);

	void generateTicketBaseOnEventConfiguration(SeatMapDto seatMapDto, String eventId);

	void deleteTicketsGeneration(String eventId);
}
