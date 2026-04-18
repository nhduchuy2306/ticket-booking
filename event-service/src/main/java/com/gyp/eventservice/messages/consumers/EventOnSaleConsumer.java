package com.gyp.eventservice.messages.consumers;

import com.gyp.common.converters.Serialization;
import com.gyp.common.enums.event.EventStatus;
import com.gyp.common.kafkatopics.TopicConstants;
import com.gyp.common.models.EventOnSaleEM;
import com.gyp.eventservice.repositories.EventRepository;
import com.gyp.eventservice.services.EventCacheService;
import com.gyp.eventservice.services.SeatInventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventOnSaleConsumer {
	private final EventRepository eventRepository;
	private final SeatInventoryService seatInventoryService;
	private final EventCacheService eventCacheService;

	@KafkaListener(topics = TopicConstants.EVENT_ON_SALE_EVENT)
	public void updateEvent(String eventOnSaleEMString) {
		try {
			EventOnSaleEM eventOnSaleEM = Serialization.deserializeFromString(
					eventOnSaleEMString, EventOnSaleEM.class);
			log.info("Received event status data: {}", eventOnSaleEM);
			if(StringUtils.isNotEmpty(eventOnSaleEM.getEventId())) {
				var event = eventRepository.findById(eventOnSaleEM.getEventId());
				if(event.isPresent()) {
					var newEvent = event.get();
					newEvent.setStatus(eventOnSaleEM.getStatus());
					eventRepository.save(newEvent);
					eventCacheService.evictBookingLists(newEvent.getOrganizationId());
					eventCacheService.evictEvent(newEvent.getId());
					if(EventStatus.ON_SALE.equals(eventOnSaleEM.getStatus())) {
						seatInventoryService.initializeSeatsForEvent(newEvent.getId());
						eventCacheService.evictSeatAvailability(newEvent.getId());
						seatInventoryService.getSeatAvailability(newEvent.getId());
					}
				}
			}
			log.info("Event updated successfully with ticket status data.");
		} catch(Exception e) {
			log.error("Failed to update event with ticket status data", e);
			throw new RuntimeException("Failed to process event status data", e);
		}
	}
}
