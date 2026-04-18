package com.gyp.eventservice.services.impl;

import java.time.LocalDateTime;
import java.util.List;

import com.gyp.common.enums.event.EventStatus;
import com.gyp.eventservice.entities.EventEntity;
import com.gyp.eventservice.repositories.EventRepository;
import com.gyp.eventservice.services.EventCacheService;
import com.gyp.eventservice.services.SeatInventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventSaleScheduler {
	private final EventRepository eventRepository;
	private final SeatInventoryService seatInventoryService;
	private final EventCacheService eventCacheService;

	@Scheduled(fixedDelay = 60_000)
	@Transactional
	public void promotePublishedEventsToSale() {
		List<EventEntity> eventsReadyToSell = eventRepository.findAllPublishedEventsReadyToSell(LocalDateTime.now());
		if(eventsReadyToSell.isEmpty()) {
			return;
		}

		for(EventEntity eventEntity : eventsReadyToSell) {
			eventEntity.setStatus(EventStatus.ON_SALE);
			eventRepository.save(eventEntity);
			seatInventoryService.initializeSeatsForEvent(eventEntity.getId());
			eventCacheService.evictSeatAvailability(eventEntity.getId());
			seatInventoryService.getSeatAvailability(eventEntity.getId());
			eventCacheService.evictBookingLists(eventEntity.getOrganizationId());
			eventCacheService.evictEvent(eventEntity.getId());
			log.info("Promoted event {} to ON_SALE and refreshed seat cache", eventEntity.getId());
		}
	}
}