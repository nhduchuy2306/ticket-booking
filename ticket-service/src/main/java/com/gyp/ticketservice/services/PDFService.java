package com.gyp.ticketservice.services;

import com.gyp.ticketservice.dtos.TicketDto;

public interface PDFService {
	byte[] generateTicketPDF(TicketDto ticket);
}
