package com.gyp.ticket.ticketservice.services;

import java.awt.image.BufferedImage;

import com.gyp.ticket.ticketservice.dtos.TicketDto;

public interface TicketDeliveryService {
	void sendByEmail(TicketDto ticket);
	byte[] generatePdf(TicketDto ticket);
	BufferedImage generateQrCode(TicketDto ticket);
}
