package com.gyp.ticket.eventservice.messages.consumers;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.gyp.common.constants.TopicConstants;
import com.gyp.common.converters.Serialization;
import com.gyp.common.models.UserAccountModel;
import com.gyp.ticket.eventservice.services.OrganizerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrganizerConsumer {
	private final OrganizerService organizerService;

	@KafkaListener(topics = TopicConstants.SYNC_USER_ACCOUNT_TOPIC)
	public void syncOrganizer(String accountResponseString) {
		try {
			List<UserAccountModel> userAccountModels = Serialization.deserializeFromString(
					accountResponseString, new TypeReference<>() {}
			);
			log.info("Receive event: {}", userAccountModels.size());
			organizerService.syncOrganizer(userAccountModels);
			log.info("Sync Successfully");
		} catch(JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}
