package com.gyp.salechannelservice.messages.consumers;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.gyp.common.converters.Serialization;
import com.gyp.common.kafkatopics.EventServiceTopic;
import com.gyp.common.models.EventEventModel;
import com.gyp.salechannelservice.services.EventInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventConsumer {
	private final EventInfoService eventInfoService;

	@KafkaListener(topics = EventServiceTopic.EVENT_SYNC)
	public void syncEvent(String EventResponseString) {
		try {
			List<EventEventModel> eventEventModels = Serialization.deserializeFromString(
					EventResponseString, new TypeReference<>() {}
			);
			log.info("Receive event: {}", eventEventModels.size());
			eventInfoService.syncEvent(eventEventModels);
			log.info("Sync successfully");
		} catch(JsonProcessingException e) {
			log.error("Sync fail", e);
			throw new RuntimeException(e);
		}
	}
}
