package com.gyp.authservice.messages.producers;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gyp.authservice.services.UserAccountService;
import com.gyp.common.constants.TopicConstants;
import com.gyp.common.converters.Serialization;
import com.gyp.common.models.UserAccountModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserAccountProducer {
	private final KafkaTemplate<String, String> kafkaTemplate;
	private final UserAccountService userAccountService;

	public void syncUserAccount() {
		try {
			List<UserAccountModel> accountResponses = userAccountService.getOrganizerAccounts();
			String dataString = Serialization.serializeToString(accountResponses);
			kafkaTemplate.send(TopicConstants.SYNC_USER_ACCOUNT_TOPIC, dataString);
		} catch(JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	public void createOrganizer(UserAccountModel model) {
		try {
			String dataString = Serialization.serializeToString(model);
			kafkaTemplate.send(TopicConstants.CREATE_ORGANIZER, dataString);
		} catch(JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	@EventListener(ApplicationReadyEvent.class)
	public void onStartUp() {
		syncUserAccount();
	}
}
