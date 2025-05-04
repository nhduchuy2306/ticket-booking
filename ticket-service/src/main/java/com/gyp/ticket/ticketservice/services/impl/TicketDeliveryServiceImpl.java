package com.gyp.ticket.ticketservice.services.impl;

import java.awt.image.BufferedImage;

import com.gyp.ticket.ticketservice.dtos.TicketDto;
import com.gyp.ticket.ticketservice.repositories.TicketRepository;
import com.gyp.ticket.ticketservice.services.TicketDeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketDeliveryServiceImpl implements TicketDeliveryService {
	private final TicketRepository ticketRepository;

	@Override
	public void sendByEmail(TicketDto ticket) {

	}

	@Override
	public byte[] generatePdf(TicketDto ticket) {
		return new byte[0];
	}

	@Override
	public BufferedImage generateQrCode(TicketDto ticket) {
		return null;
	}
}
