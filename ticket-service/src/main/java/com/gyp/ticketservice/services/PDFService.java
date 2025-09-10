package com.gyp.ticketservice.services;

import com.gyp.ticketservice.dtos.ticketgeneration.TicketGenerationResponseDto;

public interface PDFService {
	byte[] generateTicketPDF(TicketGenerationResponseDto ticket);
}
