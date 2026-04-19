package com.gyp.ticketservice.services.impl;

import java.time.Duration;
import java.util.List;

import jakarta.transaction.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gyp.common.dtos.pagination.PaginatedDto;
import com.gyp.common.enums.event.EventStatus;
import com.gyp.common.enums.event.TicketStatus;
import com.gyp.common.exceptions.ResourceNotFoundException;
import com.gyp.common.models.EventOnSaleEM;
import com.gyp.common.services.AbstractService;
import com.gyp.ticketservice.dtos.ticket.TicketResponseDto;
import com.gyp.ticketservice.entities.TicketEntity;
import com.gyp.ticketservice.mappers.TicketMapper;
import com.gyp.ticketservice.messages.producers.EventOnSaleProducer;
import com.gyp.ticketservice.repositories.TicketRepository;
import com.gyp.ticketservice.services.TicketCacheRedisService;
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
	private static final Duration TICKET_BY_ID_TTL = Duration.ofMinutes(15);
	private static final Duration TICKETS_BY_EVENT_TTL = Duration.ofMinutes(5);
	private static final Duration AVAILABLE_TICKETS_TTL = Duration.ofMinutes(1);
	private static final Duration ALL_TICKETS_TTL = Duration.ofSeconds(30);
	private static final String TICKET_BY_ID_PREFIX = "ticket:id:";
	private static final String TICKETS_BY_EVENT_PREFIX = "ticket:event:";
	private static final String AVAILABLE_TICKETS_PREFIX = "ticket:available:event:";
	private static final String SEARCH_TICKETS_PREFIX = "ticket:search:";

	private final TicketRepository ticketRepository;
	private final TicketMapper ticketMapper;
	private final EventOnSaleProducer eventOnSaleProducer;
	private final TicketCacheRedisService ticketCacheRedisService;

	@Override
	public TicketResponseDto getTicketById(String id) {
		if(StringUtils.isBlank(id)) {
			return null;
		}

		String cacheKey = buildTicketByIdKey(id);
		TicketResponseDto cachedTicket = ticketCacheRedisService.get(cacheKey, TicketResponseDto.class);
		if(cachedTicket != null) {
			return cachedTicket;
		}

		TicketEntity ticketEntity = ticketRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Ticket not found with id: " + id));
		TicketResponseDto ticketResponseDto = ticketMapper.toResponse(ticketEntity);
		ticketCacheRedisService.put(cacheKey, ticketResponseDto, TICKET_BY_ID_TTL);
		return ticketResponseDto;
	}

	@Override
	public List<TicketResponseDto> getTicketByEventId(String eventId) {
		if(StringUtils.isBlank(eventId)) {
			return null;
		}

		String cacheKey = buildTicketsByEventKey(eventId);
		List<TicketResponseDto> cachedTickets = ticketCacheRedisService.get(cacheKey,
				new TypeReference<>() {});
		if(cachedTickets != null) {
			return cachedTickets;
		}

		List<TicketEntity> ticketEntities = ticketRepository.findAllByEventId(eventId);
		List<TicketResponseDto> ticketResponses = CollectionUtils.isNotEmpty(ticketEntities)
				? ticketMapper.toResponseList(ticketEntities)
				: List.of();
		ticketCacheRedisService.put(cacheKey, ticketResponses, TICKETS_BY_EVENT_TTL);
		return ticketResponses;
	}

	@Override
	public List<TicketResponseDto> getAvailableTicketsByEventId(String eventId, String organizationId) {
		if(StringUtils.isBlank(eventId)) {
			return null;
		}

		String cacheKey = buildAvailableTicketsKey(eventId, organizationId);
		List<TicketResponseDto> cachedTickets = ticketCacheRedisService.get(cacheKey,
				new TypeReference<>() {});
		if(cachedTickets != null) {
			return cachedTickets;
		}

		TicketSearchCriteria criteria = TicketSearchCriteria.builder()
				.eventId(eventId)
				.status(TicketStatus.ON_SALE.name())
				.organizationId(organizationId)
				.build();
		List<TicketResponseDto> availableTickets = getAllTickets(criteria, null);
		if(availableTickets == null) {
			availableTickets = List.of();
		}
		ticketCacheRedisService.put(cacheKey, availableTickets, AVAILABLE_TICKETS_TTL);
		return availableTickets;
	}

	@Override
	public List<TicketResponseDto> getAllTickets(TicketSearchCriteria criteria, PaginatedDto pagination) {
		String cacheKey = buildAllTicketsKey(criteria, pagination);
		List<TicketResponseDto> cachedTickets = ticketCacheRedisService.get(cacheKey,
				new TypeReference<>() {});
		if(cachedTickets != null) {
			return cachedTickets;
		}

		Specification<TicketEntity> ticketSpecification = TicketSpecification.createTicketSpecification(criteria);
		List<TicketResponseDto> ticketResponses;
		if(pagination != null) {
			Pageable pageable = PageRequest.of(pagination.getPage(), pagination.getSize());
			var tickets = ticketRepository.findAll(ticketSpecification, pageable);
			ticketResponses = CollectionUtils.isNotEmpty(tickets.getContent())
					? ticketMapper.toResponseList(tickets.getContent())
					: List.of();
		} else {
			var tickets = ticketRepository.findAll(ticketSpecification);
			ticketResponses = CollectionUtils.isNotEmpty(tickets)
					? ticketMapper.toResponseList(tickets)
					: List.of();
		}

		ticketCacheRedisService.put(cacheKey, ticketResponses, ALL_TICKETS_TTL);
		return ticketResponses;
	}

	@Transactional(rollbackOn = Exception.class)
	@Override
	public void startSaleTicket(String eventId) {
		if(StringUtils.isNotEmpty(eventId)) {
			List<TicketEntity> tickets = ticketRepository.findAllByEventId(eventId);
			if(CollectionUtils.isEmpty(tickets)) {
				throw new ResourceNotFoundException("No tickets found for event ID: " + eventId);
			}
			int updated = ticketRepository.updateTicketStatusByEventId(eventId, TicketStatus.ON_SALE);
			if(updated == 0) {
				throw new ResourceNotFoundException("No tickets found for event ID: " + eventId);
			}
			evictTicketCaches(eventId, tickets);
			eventOnSaleProducer.send(new EventOnSaleEM(EventStatus.ON_SALE, eventId));
		} else {
			throw new IllegalArgumentException("Event ID cannot be null or empty");
		}
	}

	private String buildTicketByIdKey(String ticketId) {
		return TICKET_BY_ID_PREFIX + ticketId;
	}

	private String buildTicketsByEventKey(String eventId) {
		return TICKETS_BY_EVENT_PREFIX + eventId;
	}

	private String buildAvailableTicketsKey(String eventId, String organizationId) {
		String safeOrganizationId = StringUtils.defaultIfBlank(organizationId, "global");
		return AVAILABLE_TICKETS_PREFIX + eventId + ":" + safeOrganizationId;
	}

	private String buildAllTicketsKey(TicketSearchCriteria criteria, PaginatedDto pagination) {
		String eventId = normalizeCacheSegment(criteria == null ? null : criteria.getEventId());
		String ticketId = normalizeCacheSegment(criteria == null ? null : criteria.getTicketId());
		String organizationId = normalizeCacheSegment(criteria == null ? null : criteria.getOrganizationId());
		String status = normalizeCacheSegment(criteria == null ? null : criteria.getStatus());
		String ticketType = normalizeCacheSegment(criteria == null ? null : criteria.getTicketType());
		String seatNumber = normalizeCacheSegment(criteria == null ? null : criteria.getSeatNumber());
		String orderBy = normalizeCacheSegment(criteria == null ? null : criteria.getOrderBy());
		String page = pagination == null ? "all" : String.valueOf(pagination.getPage());
		String size = pagination == null ? "all" : String.valueOf(pagination.getSize());

		return SEARCH_TICKETS_PREFIX + "event:" + eventId + ":ticket:" + ticketId + ":org:" + organizationId
				+ ":status:" + status + ":type:" + ticketType + ":seat:" + seatNumber + ":order:" + orderBy
				+ ":page:" + page + ":size:" + size;
	}

	private String normalizeCacheSegment(String value) {
		if(StringUtils.isBlank(value)) {
			return "all";
		}

		return StringUtils.lowerCase(StringUtils.trim(value))
				.replace(':', '_')
				.replace('|', '_')
				.replace(' ', '_');
	}

	private void evictTicketCaches(String eventId, List<TicketEntity> tickets) {
		ticketCacheRedisService.evictByPrefix(buildTicketsByEventKey(eventId));
		ticketCacheRedisService.evictByPrefix(AVAILABLE_TICKETS_PREFIX + eventId + ":");
		ticketCacheRedisService.evictByPrefix(SEARCH_TICKETS_PREFIX);
		if(CollectionUtils.isNotEmpty(tickets)) {
			for(TicketEntity ticket : tickets) {
				ticketCacheRedisService.evict(buildTicketByIdKey(ticket.getId()));
			}
		}
	}
}
