package com.gyp.ticket.ticketservice.services;

import com.gyp.ticket.ticketservice.dtos.TicketDto;

public interface PDFService {
	byte[] generateTicketPDF(TicketDto ticket);
}
