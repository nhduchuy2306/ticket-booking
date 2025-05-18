package com.gyp.ticketservice.services.impl;

import com.gyp.common.services.AbstractService;
import com.gyp.ticketservice.dtos.ticket.TicketResponseDto;
import com.gyp.ticketservice.entities.TicketEntity;
import com.gyp.ticketservice.mappers.TicketMapper;
import com.gyp.ticketservice.repositories.TicketRepository;
import com.gyp.ticketservice.services.TicketGenerationService;
import com.gyp.ticketservice.services.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl extends AbstractService implements TicketService {
	private final TicketGenerationService ticketGenerationService;
	private final TicketRepository ticketRepository;
	private final TicketMapper ticketMapper;

	@Override
	public TicketResponseDto getTicketById(String id) {
		TicketEntity ticketEntity = ticketRepository.findTicketById(id);
		if(ticketEntity != null) {
			return ticketMapper.toResponse(ticketEntity);
		}
		return null;
	}
}
