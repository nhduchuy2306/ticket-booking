package com.gyp.eventservice.messages.consumers;

import com.gyp.common.converters.Serialization;
import com.gyp.common.kafkatopics.TopicConstants;
import com.gyp.common.models.EventGenerationTicketEM;
import com.gyp.eventservice.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventGeneratedConsumer {
	private final EventRepository eventRepository;

	@KafkaListener(topics = TopicConstants.EVENT_TICKET_GENERATED)
	public void updateEvent(String eventGenerationTicketEMString) {
		try {
			EventGenerationTicketEM eventGenerationTicketEM = Serialization.deserializeFromString(
					eventGenerationTicketEMString, EventGenerationTicketEM.class);
			log.info("Received event generation data: {}", eventGenerationTicketEMString);
			if(StringUtils.isNotEmpty(eventGenerationTicketEM.getEventId())) {
				var event = eventRepository.findById(eventGenerationTicketEM.getEventId());
				if(event.isPresent()) {
					var newEvent = event.get();
					newEvent.setIsGenerated(true);
					eventRepository.save(newEvent);
				}
			}
			log.info("Event updated successfully with ticket generation data.");
		} catch(Exception e) {
			log.error("Failed to update event with ticket generation data", e);
			throw new RuntimeException("Failed to process event generation data", e);
		}
	}
}
