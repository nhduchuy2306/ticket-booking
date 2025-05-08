package com.gyp.ticketservice.services;

import com.gyp.ticketservice.dtos.ticket.TicketRequestDto;

public interface PDFService {
	byte[] generateTicketPDF(TicketRequestDto ticket);
}
