package com.gyp.ticketservice.services.impl;

import java.util.List;

import jakarta.transaction.Transactional;

import com.gyp.common.dtos.pagination.PaginatedDto;
import com.gyp.common.enums.event.TicketStatus;
import com.gyp.common.exceptions.ResourceNotFoundException;
import com.gyp.common.services.AbstractService;
import com.gyp.ticketservice.dtos.ticket.TicketResponseDto;
import com.gyp.ticketservice.entities.TicketEntity;
import com.gyp.ticketservice.mappers.TicketMapper;
import com.gyp.ticketservice.repositories.TicketRepository;
import com.gyp.ticketservice.services.TicketService;
import com.gyp.ticketservice.services.criteria.TicketSearchCriteria;
import com.gyp.ticketservice.services.specification.TicketSpecification;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl extends AbstractService implements TicketService {
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
		if(CollectionUtils.isNotEmpty(ticketEntities)) {
			return ticketMapper.toResponseList(ticketEntities);
		}
		return null;
	}

	@Override
	public List<TicketResponseDto> getAllTickets(TicketSearchCriteria criteria, PaginatedDto pagination) {
		Specification<TicketEntity> ticketSpecification = TicketSpecification.createTicketSpecification(criteria);
		if(pagination != null) {
			Pageable pageable = PageRequest.of(pagination.getPage(), pagination.getSize());
			var tickets = ticketRepository.findAll(ticketSpecification, pageable);
			if(CollectionUtils.isNotEmpty(tickets.getContent())) {
				return ticketMapper.toResponseList(tickets.getContent());
			}
		}
		var tickets = ticketRepository.findAll(ticketSpecification);
		if(CollectionUtils.isNotEmpty(tickets)) {
			return ticketMapper.toResponseList(tickets);
		}
		return null;
	}

	@Transactional(rollbackOn = Exception.class)
	@Override
	public void startSaleTicket(String eventId) {
		if(StringUtils.isNotEmpty(eventId)) {
			int updated = ticketRepository.updateTicketStatusByEventId(eventId, TicketStatus.ON_SALE);
			if(updated == 0) {
				throw new ResourceNotFoundException("No tickets found for event ID: " + eventId);
			}
		} else {
			throw new IllegalArgumentException("Event ID cannot be null or empty");
		}
	}
}
