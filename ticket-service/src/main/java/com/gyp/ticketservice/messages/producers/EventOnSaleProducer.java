package com.gyp.ticketservice.messages.producers;

import com.gyp.common.converters.Serialization;
import com.gyp.common.kafkatopics.TopicConstants;
import com.gyp.common.models.EventOnSaleEM;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventOnSaleProducer {
	private final KafkaTemplate<String, String> kafkaTemplate;

	public void send(EventOnSaleEM eventOnSaleEM) {
		String dataString = Serialization.serializeToString(eventOnSaleEM);
		kafkaTemplate.send(TopicConstants.EVENT_ON_SALE_EVENT, dataString)
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
	}
}
