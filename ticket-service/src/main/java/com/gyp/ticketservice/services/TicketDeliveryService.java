package com.gyp.ticketservice.services;

import java.awt.image.BufferedImage;

import com.gyp.ticketservice.dtos.TicketDto;

public interface TicketDeliveryService {
	void sendByEmail(TicketDto ticket);
	byte[] generatePdf(TicketDto ticket);
	BufferedImage generateQrCode(TicketDto ticket);
}
