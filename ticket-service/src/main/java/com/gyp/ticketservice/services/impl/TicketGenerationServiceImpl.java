package com.gyp.ticketservice.services.impl;

import com.gyp.common.exceptions.ResourceNotFoundException;
import com.gyp.common.models.EventGenerationTicketEM;
import com.gyp.common.utils.SecurityUtils;
import com.gyp.ticketservice.dtos.ticketgeneration.TicketGenerationResponseDto;
import com.gyp.ticketservice.dtos.ticketgeneration.TicketGenerationSummaryDto;
import com.gyp.ticketservice.entities.TicketEntity;
import com.gyp.ticketservice.messages.producers.EventGeneratedProducer;
import com.gyp.ticketservice.repositories.TicketRepository;
import com.gyp.ticketservice.services.TicketCacheRedisService;
import com.gyp.ticketservice.services.TicketGenerationService;
import com.gyp.ticketservice.services.criteria.TicketSearchCriteria;
import com.gyp.ticketservice.services.specification.TicketSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketGenerationServiceImpl implements TicketGenerationService {
	private static final long VALIDATE_TICKET_TTL_SECONDS = 300L;
	private static final long TICKET_GENERATION_TTL_SECONDS = 900L;
	private static final String TICKET_BY_ID_PREFIX = "ticket:id:";
	private static final String TICKET_CODE_PREFIX = "ticket:code:";
	private static final String TICKET_GENERATION_PREFIX = "ticket:generation:";
	private static final String TICKETS_BY_EVENT_PREFIX = "ticket:event:";
	private static final String AVAILABLE_TICKETS_PREFIX = "ticket:available:event:";
	private static final String SEARCH_TICKETS_PREFIX = "ticket:search:";

	private final TicketRepository ticketRepository;
	private final EventGeneratedProducer eventGeneratedProducer;
	private final TicketCacheRedisService ticketCacheRedisService;

	@Override
	public TicketGenerationSummaryDto validateTicket(String ticketNumber) {
		if(StringUtils.isBlank(ticketNumber)) {
			return null;
		}

		String cacheKey = buildTicketCodeKey(ticketNumber);
		TicketGenerationSummaryDto cachedTicket = ticketCacheRedisService.get(cacheKey, TicketGenerationSummaryDto.class);
		if(cachedTicket != null) {
			return cachedTicket;
		}

		TicketEntity ticketEntity = ticketRepository.findByTicketCode(ticketNumber).orElse(null);
		if(ticketEntity == null) {
			return null;
		}

		TicketGenerationSummaryDto summaryDto = new TicketGenerationSummaryDto(
				ticketEntity.getAttendeeName(),
				ticketEntity.getTicketCode(),
				ticketEntity.getSeatInfo(),
				ticketEntity.getSeatId()
		);
		ticketCacheRedisService.put(cacheKey, summaryDto, java.time.Duration.ofSeconds(VALIDATE_TICKET_TTL_SECONDS));
		return summaryDto;
	}

	@Override
	public TicketGenerationResponseDto getTicketGenerationById(String id) {
		if(StringUtils.isBlank(id)) {
			return null;
		}

		String cacheKey = buildTicketGenerationByIdKey(id);
		TicketGenerationResponseDto cachedTicket = ticketCacheRedisService.get(cacheKey, TicketGenerationResponseDto.class);
		if(cachedTicket != null) {
			return cachedTicket;
		}

		TicketEntity ticketEntity = ticketRepository.findById(id).orElse(null);
		if(ticketEntity == null) {
			return null;
		}

		TicketGenerationResponseDto responseDto = toTicketGenerationResponseDto(ticketEntity);
		ticketCacheRedisService.put(cacheKey, responseDto, java.time.Duration.ofSeconds(TICKET_GENERATION_TTL_SECONDS));
		return responseDto;
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
				evictTicketCaches(eventId, tickets);
				evictGenerationCaches(tickets);
				ticketCacheRedisService.evictByPrefix(SEARCH_TICKETS_PREFIX);
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

	private void evictTicketCaches(String eventId, java.util.List<TicketEntity> tickets) {
		ticketCacheRedisService.evictByPrefix(TICKETS_BY_EVENT_PREFIX + eventId);
		ticketCacheRedisService.evictByPrefix(AVAILABLE_TICKETS_PREFIX + eventId + ":");
		ticketCacheRedisService.evictByPrefix(SEARCH_TICKETS_PREFIX);
		for(TicketEntity ticket : tickets) {
			ticketCacheRedisService.evict(TICKET_BY_ID_PREFIX + ticket.getId());
			ticketCacheRedisService.evict(buildTicketCodeKey(ticket.getTicketCode()));
			ticketCacheRedisService.evict(buildTicketGenerationByIdKey(ticket.getId()));
		}
	}

	private void evictGenerationCaches(java.util.List<TicketEntity> tickets) {
		for(TicketEntity ticket : tickets) {
			ticketCacheRedisService.evict(buildTicketGenerationByIdKey(ticket.getId()));
			ticketCacheRedisService.evict(buildTicketCodeKey(ticket.getTicketCode()));
		}
	}

	private String buildTicketCodeKey(String ticketCode) {
		return TICKET_CODE_PREFIX + StringUtils.trimToEmpty(ticketCode);
	}

	private String buildTicketGenerationByIdKey(String id) {
		return TICKET_GENERATION_PREFIX + StringUtils.trimToEmpty(id);
	}

	private TicketGenerationResponseDto toTicketGenerationResponseDto(TicketEntity ticketEntity) {
		return TicketGenerationResponseDto.builder()
				.id(ticketEntity.getId())
				.eventId(ticketEntity.getEventId())
				.eventName(ticketEntity.getEventName())
				.seatId(ticketEntity.getSeatId())
				.seatInfo(ticketEntity.getSeatInfo())
				.ticketNumber(ticketEntity.getTicketCode())
				.attendeeName(ticketEntity.getAttendeeName())
				.attendeeEmail(ticketEntity.getAttendeeEmail())
				.eventDateTime(ticketEntity.getEventDateTime())
				.status(ticketEntity.getStatus())
				.ticketEntity(null)
				.build();
	}
}
