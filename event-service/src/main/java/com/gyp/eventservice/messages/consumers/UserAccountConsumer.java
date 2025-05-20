package com.gyp.eventservice.messages.consumers;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.gyp.common.converters.Serialization;
import com.gyp.common.kafkatopics.AuthServiceTopic;
import com.gyp.common.models.UserAccountEventModel;
import com.gyp.eventservice.services.OrganizerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserAccountConsumer {
	private final OrganizerService organizerService;

	@KafkaListener(topics = AuthServiceTopic.USER_ACCOUNT_SYNC)
	public void syncOrganizer(String accountResponseString) {
		try {
			List<UserAccountEventModel> userAccountEventModels = Serialization.deserializeFromString(
					accountResponseString, new TypeReference<>() {}
			);
			log.info("Receive event: {}", userAccountEventModels.size());
			organizerService.syncOrganizer(userAccountEventModels);
			log.info("Sync Successfully");
		} catch(JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}
