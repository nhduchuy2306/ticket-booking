package com.gyp.eventservice.messages.producers;

import java.util.List;

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
		try {
			List<EventEventModel> eventEventModels = eventService.getListEventModel();
			String dataString = Serialization.serializeToString(eventEventModels);
			kafkaTemplate.send(EventServiceTopic.EVENT_SYNC, dataString);
			log.info("Sent sync Event data");
		} catch(JsonProcessingException e) {
			log.info("Sent sync fail", e);
			throw new RuntimeException(e);
		}
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
