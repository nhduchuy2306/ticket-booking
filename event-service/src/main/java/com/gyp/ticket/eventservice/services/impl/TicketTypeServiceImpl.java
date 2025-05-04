package com.gyp.ticket.eventservice.services.impl;

import com.gyp.ticket.eventservice.dtos.seatmap.Seat;
import com.gyp.ticket.eventservice.entities.TicketTypeEntity;
import com.gyp.ticket.eventservice.repositories.TicketTypeRepository;
import com.gyp.ticket.eventservice.services.TicketTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketTypeServiceImpl implements TicketTypeService {
	private final TicketTypeRepository ticketTypeRepository;

	@Override
	public double getEffectivePrice(Seat seat) {
		if(seat.getPrice() > 0) {
			return seat.getPrice();
		}
		TicketTypeEntity ticketType = ticketTypeRepository.findById(seat.getTicketTypeId())
				.orElseThrow(() -> new RuntimeException("TicketType not found"));
		return ticketType.getPrice();
	}
}
