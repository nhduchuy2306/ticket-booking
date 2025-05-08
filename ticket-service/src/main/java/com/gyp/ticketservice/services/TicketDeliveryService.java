package com.gyp.ticketservice.services;

import java.awt.image.BufferedImage;

import com.gyp.ticketservice.dtos.ticket.TicketRequestDto;

public interface TicketDeliveryService {
	void sendByEmail(TicketRequestDto ticket);
	byte[] generatePdf(TicketRequestDto ticket);
	BufferedImage generateQrCode(TicketRequestDto ticket);
}
