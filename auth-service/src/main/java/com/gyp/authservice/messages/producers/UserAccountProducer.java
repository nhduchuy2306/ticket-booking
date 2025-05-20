package com.gyp.authservice.messages.producers;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gyp.authservice.services.UserAccountService;
import com.gyp.common.converters.Serialization;
import com.gyp.common.kafkatopics.AuthServiceTopic;
import com.gyp.common.models.UserAccountEventModel;
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
public class UserAccountProducer {
	@Value("${app.sync-on-startup:false}")
	private boolean syncOnStartup;

	private final KafkaTemplate<String, String> kafkaTemplate;
	private final UserAccountService userAccountService;

	public void syncUserAccount() {
		try {
			List<UserAccountEventModel> accountResponses = userAccountService.getOrganizerAccounts();
			String dataString = Serialization.serializeToString(accountResponses);
			kafkaTemplate.send(AuthServiceTopic.USER_ACCOUNT_SYNC, dataString);
			log.info("Sent sync user account");
		} catch(JsonProcessingException e) {
			log.error("Fail", e);
			throw new RuntimeException(e);
		}
	}

	public void createOrganizer(UserAccountEventModel model) {
		try {
			String dataString = Serialization.serializeToString(model);
			kafkaTemplate.send(AuthServiceTopic.ORGANIZER_CREATE_COMMAND, dataString);
		} catch(JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	@EventListener(ApplicationReadyEvent.class)
	public void onStartUp() {
		if(syncOnStartup) {
			syncUserAccount();
		} else {
			log.info("Skipping user account sync on startup in development environment");
		}
	}
}
