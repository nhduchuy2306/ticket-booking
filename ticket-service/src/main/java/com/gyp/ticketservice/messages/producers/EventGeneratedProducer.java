package com.gyp.ticketservice.messages.producers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gyp.common.converters.Serialization;
import com.gyp.common.kafkatopics.TopicConstants;
import com.gyp.common.models.EventGenerationTicketEM;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventGeneratedProducer {
	private final KafkaTemplate<String, String> kafkaTemplate;

	public void send(EventGenerationTicketEM eventGenerationTicketEM) {
		try {
			String dataString = Serialization.serializeToString(eventGenerationTicketEM);
			kafkaTemplate.send(TopicConstants.EVENT_TICKET_GENERATED, dataString)
					.whenComplete((result, throwable) -> {
						if(throwable != null) {
							log.error("Failed to send message to topic: {}", throwable.getMessage());
						} else {
							log.info("Message sent successfully to topic at offset {} in partition {}",
									result.getRecordMetadata().offset(),
									result.getRecordMetadata().partition());
							log.info("Sent event generation data: {}", dataString);
						}
					});
		} catch(JsonProcessingException e) {
			log.error("Serialization failed", e);
			throw new RuntimeException("Failed to send event generation data", e);
		}
	}
}
