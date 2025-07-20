package com.gyp.ticketservice.services;

import com.gyp.ticketservice.dtos.ticketgeneration.TicketGenerationResponseDto;
import com.gyp.ticketservice.dtos.ticketgeneration.TicketGenerationSummaryDto;

public interface TicketGenerationService {
	TicketGenerationSummaryDto validateTicket(String ticketNumber);

	TicketGenerationResponseDto getTicketGenerationById(String id);

	void generateTicketBaseOnEventConfiguration(String eventId);
}
