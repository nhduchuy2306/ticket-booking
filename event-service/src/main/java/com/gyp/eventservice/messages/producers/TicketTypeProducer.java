package com.gyp.eventservice.messages.producers;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import jakarta.annotation.PostConstruct;

import com.gyp.common.converters.Serialization;
import com.gyp.common.kafkatopics.EventServiceTopic;
import com.gyp.common.models.TicketTypeEventModel;
import com.gyp.eventservice.services.TicketTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TicketTypeProducer {
	@Value("${app.sync-on-startup:false}")
	private boolean syncOnStartup;

	private final KafkaTemplate<String, String> kafkaTemplate;
	private final TicketTypeService ticketTypeService;

	public void syncTicketType() {
		try {
			List<TicketTypeEventModel> ticketTypeModels = ticketTypeService.getListTicketTypeModel();
			String dataString = Serialization.serializeToString(ticketTypeModels);

			CompletableFuture<SendResult<String, String>> future =
					kafkaTemplate.send(EventServiceTopic.TICKET_TYPE_SYNC, dataString);

			future.whenComplete((result, throwable) -> {
				if(throwable != null) {
					log.error("Failed to send message to topic {}: {}", EventServiceTopic.TICKET_TYPE_SYNC,
							throwable.getMessage());
				} else {
					log.info("Message sent successfully to topic {} at offset {} in partition {}",
							result.getRecordMetadata().topic(),
							result.getRecordMetadata().offset(),
							result.getRecordMetadata().partition());
					log.info("Sent sync TicketType data: {}", dataString);
				}
			});
		} catch(Exception e) {
			log.error("Serialization failed", e);
			throw new RuntimeException("Failed to sync TicketType data", e);
		}
	}

	@PostConstruct
	public void init() {
		log.info("TicketTypeProducer initialized, listening on topic: {}", EventServiceTopic.TICKET_TYPE_SYNC);
		if(syncOnStartup) {
			syncTicketType();
		}
	}

	@EventListener(ApplicationReadyEvent.class)
	public void onStartUp() {
		if(syncOnStartup) {
			syncTicketType();
		}
	}
}
