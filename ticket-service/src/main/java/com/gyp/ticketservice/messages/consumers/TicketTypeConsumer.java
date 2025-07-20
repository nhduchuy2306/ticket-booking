package com.gyp.ticketservice.messages.consumers;

import com.gyp.common.kafkatopics.EventServiceTopic;
import com.gyp.ticketservice.services.TicketTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TicketTypeConsumer {
	private final TicketTypeService ticketTypeService;

	@KafkaListener(topics = EventServiceTopic.TICKET_TYPE_SYNC)
	public void syncTicketType(String ticketTypeResponseString) {
		log.info("Received ticket type sync event: {}", ticketTypeResponseString);
		log.info("Ticket types synced successfully");
	}
}
