package com.gyp.salechannelservice.messages.consumers;

import java.util.List;

import com.gyp.common.kafkatopics.TopicConstants;
import com.gyp.salechannelservice.services.SaleChannelEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AssignSaleChannelToEventConsumer {
	private final SaleChannelEventService saleChannelEventService;

	@KafkaListener(topics = TopicConstants.ASSIGN_SALE_CHANNEL_TO_EVENT)
	public void assignSaleChannelToEventConsumer(@Header(KafkaHeaders.RECEIVED_KEY) String eventId,
			@Payload String saleChannelIds) {
		try {
			log.info("Received sale channels for event {}: {}", eventId, saleChannelIds);

			if(StringUtils.isEmpty(eventId) || StringUtils.isEmpty(saleChannelIds)) {
				log.warn("Event ID or Sale Channel IDs are empty. Skipping processing.");
				return;
			}
			List<String> saleChannelIdList = List.of(saleChannelIds.split(","));

			if(CollectionUtils.isEmpty(saleChannelIdList)) {
				log.info("No existing sale channels found for event {}. Creating new entries.", eventId);

				saleChannelIdList.forEach(saleChannelId -> {
					saleChannelEventService.removeEventFromChannel(saleChannelId, eventId);
				});
			} else {
				log.info("Found existing sale channels for event {}: {}", eventId, saleChannelIdList);

				saleChannelIdList.forEach(saleChannelId -> {
					saleChannelEventService.removeEventFromChannel(saleChannelId, eventId);
				});

				saleChannelIdList.forEach(saleChannelId -> {
					saleChannelEventService.assignEventToChannel(saleChannelId, eventId);
				});
			}
		} catch(Exception e) {
			log.error("Failed to process sale channels for event {}: {}", eventId, e.getMessage());
			throw new RuntimeException("Failed to assign sale channels to event", e);
		}
	}
}
