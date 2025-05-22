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
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventConsumer {
	private final EventInfoService eventInfoService;

	@KafkaListener(
			id = EventServiceTopic.EVENT_SYNC,
			topics = EventServiceTopic.EVENT_SYNC,
			groupId = "sale-channel-service-group")
	public void syncEvent(String eventResponseString) {
		try {
			List<EventEventModel> eventEventModels = Serialization.deserializeFromString(
					eventResponseString, new TypeReference<>() {}
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
