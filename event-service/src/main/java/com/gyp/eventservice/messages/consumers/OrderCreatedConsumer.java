package com.gyp.eventservice.messages.consumers;

import com.gyp.common.annotations.KafkaComponent;
import com.gyp.common.converters.Serialization;
import com.gyp.common.kafkatopics.TopicConstants;
import com.gyp.common.models.OrderCreatedEM;
import com.gyp.eventservice.services.SeatInventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.kafka.annotation.KafkaListener;

@Slf4j
@KafkaComponent
@RequiredArgsConstructor
public class OrderCreatedConsumer {
	private final SeatInventoryService seatInventoryService;

	@KafkaListener(topics = TopicConstants.ORDER_CREATED_EVENT)
	public void updateEvent(String orderCreatedEMString) {
		try {
			OrderCreatedEM orderCreatedEM = Serialization.deserializeFromString(
					orderCreatedEMString, OrderCreatedEM.class);
			if(StringUtils.isEmpty(orderCreatedEM.getEventId()) || orderCreatedEM.getSeatIds() == null
					|| orderCreatedEM.getSeatIds().isEmpty()) {
				log.warn("Skipping invalid order-created event payload: {}", orderCreatedEMString);
				return;
			}
			if(StringUtils.isNotEmpty(orderCreatedEM.getEventId())) {
				seatInventoryService.reserveSeatsForOrder(orderCreatedEM.getEventId(), orderCreatedEM.getOrderId(),
						orderCreatedEM.getCustomerEmail(), orderCreatedEM.getSeatIds());
				log.info("Seats reserved for order ID: {}", orderCreatedEM.getOrderId());
			}
		} catch(Exception e) {
			log.error("Failed to reserve seats for order created event", e);
			throw new RuntimeException("Failed to process order created event", e);
		}
	}
}
