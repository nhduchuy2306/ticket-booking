package com.gyp.eventservice.messages.producers;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.gyp.common.kafkatopics.TopicConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AssignSaleChannelToEventProducer {
	private final KafkaTemplate<String, String> kafkaTemplate;

	public void assignSaleChannelToEventProducer(String eventId, List<String> saleChannelIds) {
		try {
			String dataString = StringUtils.EMPTY;
			if(CollectionUtils.isNotEmpty(saleChannelIds)) {
				dataString = String.join(",", saleChannelIds);
			}
			CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(
					TopicConstants.ASSIGN_SALE_CHANNEL_TO_EVENT, eventId, dataString);
			future.whenComplete((result, throwable) -> {
				if(throwable != null) {
					log.error("Failed to send sale channels for event {}: {}", eventId, throwable.getMessage());
				} else {
					log.info("Sale channels for event {} sent successfully to topic {} at offset {} in partition {}",
							eventId,
							result.getRecordMetadata().topic(),
							result.getRecordMetadata().offset(),
							result.getRecordMetadata().partition());
				}
			});
		} catch(Exception e) {
			log.error("Error while sending sale channels for event {}: {}", eventId, e.getMessage());
			throw new RuntimeException("Failed to assign sale channels to event", e);
		}
	}
}
