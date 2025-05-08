package com.gyp.ticketservice.services.impl;

import java.awt.image.BufferedImage;

import com.gyp.ticketservice.dtos.ticket.TicketRequestDto;
import com.gyp.ticketservice.repositories.TicketRepository;
import com.gyp.ticketservice.services.TicketDeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketDeliveryServiceImpl implements TicketDeliveryService {
	private final TicketRepository ticketRepository;

	@Override
	public void sendByEmail(TicketRequestDto ticket) {

	}

	@Override
	public byte[] generatePdf(TicketRequestDto ticket) {
		return new byte[0];
	}

	@Override
	public BufferedImage generateQrCode(TicketRequestDto ticket) {
		return null;
	}
}
