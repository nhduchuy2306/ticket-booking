package com.gyp.eventservice.messages.producers;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import jakarta.annotation.PostConstruct;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gyp.common.converters.Serialization;
import com.gyp.common.kafkatopics.EventServiceTopic;
import com.gyp.common.models.EventEventModel;
import com.gyp.eventservice.services.EventService;
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
public class EventProducer {
	@Value("${app.sync-on-startup:false}")
	private boolean syncOnStartup;

	private final KafkaTemplate<String, String> kafkaTemplate;
	private final EventService eventService;

	public void syncEvent() {
//		try {
//			List<EventEventModel> eventEventModels = eventService.getListEventModel();
//			String dataString = Serialization.serializeToString(eventEventModels);
//
//			CompletableFuture<SendResult<String, String>> future =
//					kafkaTemplate.send(EventServiceTopic.EVENT_SYNC, dataString);
//
//			future.whenComplete((result, throwable) -> {
//				if(throwable != null) {
//					log.error("Failed to send message to topic {}: {}",
//							EventServiceTopic.EVENT_SYNC, throwable.getMessage());
//				} else {
//					log.info("Message sent successfully to topic {} at offset {} in partition {}",
//							result.getRecordMetadata().topic(),
//							result.getRecordMetadata().offset(),
//							result.getRecordMetadata().partition());
//					log.info("Sent sync Event data: {}", dataString);
//				}
//			});
//		} catch(JsonProcessingException e) {
//			log.error("Serialization failed", e);
//			throw new RuntimeException("Failed to sync Event data", e);
//		}
	}

	public void createNewEvent() {

	}

	@PostConstruct
	public void init() {
		log.info("EventConsumer initialized, listening on topic: {}", EventServiceTopic.EVENT_SYNC);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void onStartUp() {
		if(syncOnStartup) {
			syncEvent();
		} else {
			log.info("Skipping user account sync on startup in development environment");
		}
	}
}
