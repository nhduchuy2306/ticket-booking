package com.gyp.ticketservice.services.impl;

import java.awt.image.BufferedImage;

import com.gyp.ticketservice.dtos.TicketDto;
import com.gyp.ticketservice.repositories.TicketRepository;
import com.gyp.ticketservice.services.TicketDeliveryService;
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
