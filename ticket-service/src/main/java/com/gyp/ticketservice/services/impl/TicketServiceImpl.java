package com.gyp.ticketservice.services.impl;

import java.util.List;

import com.gyp.common.exceptions.ResourceNotFoundException;
import com.gyp.common.services.AbstractService;
import com.gyp.ticketservice.dtos.ticket.TicketResponseDto;
import com.gyp.ticketservice.entities.TicketEntity;
import com.gyp.ticketservice.mappers.TicketMapper;
import com.gyp.ticketservice.repositories.TicketRepository;
import com.gyp.ticketservice.services.TicketGenerationService;
import com.gyp.ticketservice.services.TicketService;
import com.gyp.ticketservice.services.criteria.TicketSearchCriteria;
import com.gyp.ticketservice.services.specification.TicketSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl extends AbstractService implements TicketService {
	private final TicketGenerationService ticketGenerationService;
	private final TicketRepository ticketRepository;
	private final TicketMapper ticketMapper;

	@Override
	public TicketResponseDto getTicketById(String id) {
		TicketEntity ticketEntity = ticketRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + id));
		if(ticketEntity != null) {
			return ticketMapper.toResponse(ticketEntity);
		}
		return null;
	}

	@Override
	public List<TicketResponseDto> getTicketByEventId(String eventId) {
		List<TicketEntity> ticketEntities = ticketRepository.findAllByEventId(eventId);
		if(!CollectionUtils.isEmpty(ticketEntities)) {
			return ticketMapper.toResponseList(ticketEntities);
		}
		return null;
	}

	@Override
	public List<TicketResponseDto> getAllTickets(TicketSearchCriteria criteria) {
		Specification<TicketEntity> ticketSpecification = TicketSpecification.createTicketSpecification(criteria);
		var tickets = ticketRepository.findAll(ticketSpecification);
		if(!CollectionUtils.isEmpty(tickets)) {
			return ticketMapper.toResponseList(tickets);
		}
		return null;
	}
}
