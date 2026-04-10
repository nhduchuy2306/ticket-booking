package com.gyp.ticketservice.services.impl;

import com.gyp.common.exceptions.ResourceNotFoundException;
import com.gyp.common.models.EventGenerationTicketEM;
import com.gyp.common.utils.SecurityUtils;
import com.gyp.ticketservice.dtos.ticketgeneration.TicketGenerationResponseDto;
import com.gyp.ticketservice.dtos.ticketgeneration.TicketGenerationSummaryDto;
import com.gyp.ticketservice.entities.TicketEntity;
import com.gyp.ticketservice.messages.producers.EventGeneratedProducer;
import com.gyp.ticketservice.repositories.TicketRepository;
import com.gyp.ticketservice.services.TicketGenerationService;
import com.gyp.ticketservice.services.criteria.TicketSearchCriteria;
import com.gyp.ticketservice.services.specification.TicketSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketGenerationServiceImpl implements TicketGenerationService {
	private final TicketRepository ticketRepository;
	private final EventGeneratedProducer eventGeneratedProducer;

	@Override
	public TicketGenerationSummaryDto validateTicket(String ticketNumber) {
		return null;
	}

	@Override
	public TicketGenerationResponseDto getTicketGenerationById(String id) {
		//		Optional<TicketGenerationEntity> ticketGeneration = ticketGenerationRepository.findById(id);
		//		return ticketGeneration.map(ticketGenerationMapper::toResponse).orElse(null);
		return null;
	}

	@Override
	public void deleteTicketsGeneration(String eventId) {
		try {
			String organizationId = SecurityUtils.getCurrentOrganizationId();
			TicketSearchCriteria ticketSearchCriteria = TicketSearchCriteria.builder()
					.eventId(eventId)
					.organizationId(organizationId)
					.build();
			Specification<TicketEntity> specification = TicketSpecification.createTicketSpecification(
					ticketSearchCriteria);
			var tickets = ticketRepository.findAll(specification);
			if(!CollectionUtils.isEmpty(tickets)) {
				ticketRepository.deleteAll(tickets);
				EventGenerationTicketEM eventGenerationTicketEM = EventGenerationTicketEM.builder()
						.isTicketGenerated(false)
						.eventId(eventId)
						.build();
				eventGeneratedProducer.send(eventGenerationTicketEM);
			}
		} catch(Exception e) {
			throw new ResourceNotFoundException("Error deleting tickets for event: " + eventId, e);
		}
	}
}
