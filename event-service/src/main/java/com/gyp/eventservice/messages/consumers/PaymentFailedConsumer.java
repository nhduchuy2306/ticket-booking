package com.gyp.eventservice.messages.consumers;

import com.gyp.common.annotations.KafkaComponent;
import com.gyp.common.converters.Serialization;
import com.gyp.common.kafkatopics.TopicConstants;
import com.gyp.common.models.PaymentOutcomeEM;
import com.gyp.eventservice.services.SeatInventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;

@Slf4j
@KafkaComponent
@RequiredArgsConstructor
public class PaymentFailedConsumer {
	private final SeatInventoryService seatInventoryService;

	@KafkaListener(topics = TopicConstants.PAYMENT_FAILED_EVENT)
	public void handlePaymentFailed(String message) {
		try {
			PaymentOutcomeEM paymentOutcomeEM = Serialization.deserializeFromString(message, PaymentOutcomeEM.class);
			seatInventoryService.releaseSeatsForOrder(paymentOutcomeEM.getEventId(), paymentOutcomeEM.getOrderId());
			log.info("Payment failure processed for order ID: {}", paymentOutcomeEM.getOrderId());
		} catch(Exception e) {
			log.error("Failed to process payment failed event", e);
			throw new RuntimeException("Failed to process payment failed event", e);
		}
	}
}