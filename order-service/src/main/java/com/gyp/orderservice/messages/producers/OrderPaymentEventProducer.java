package com.gyp.orderservice.messages.producers;

import java.util.concurrent.CompletableFuture;

import com.gyp.common.annotations.KafkaComponent;
import com.gyp.common.converters.Serialization;
import com.gyp.common.enums.order.PaymentStatus;
import com.gyp.common.kafkatopics.TopicConstants;
import com.gyp.common.models.PaymentOutcomeEM;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

@Slf4j
@KafkaComponent
@RequiredArgsConstructor
public class OrderPaymentEventProducer {
	private final KafkaTemplate<String, String> kafkaTemplate;

	public void sendPaymentOutcome(PaymentOutcomeEM paymentOutcomeEM) {
		String topic = PaymentStatus.SUCCESS.equals(paymentOutcomeEM.getPaymentStatus())
				? TopicConstants.PAYMENT_SUCCESS_EVENT
				: TopicConstants.PAYMENT_FAILED_EVENT;
		String payload = Serialization.serializeToString(paymentOutcomeEM);
		CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, payload);
		future.whenComplete((result, throwable) -> {
			if(throwable != null) {
				log.error("Failed to send payment outcome event for order ID {}: {}",
						paymentOutcomeEM.getOrderId(), throwable.getMessage());
			} else {
				log.info("Payment outcome event sent successfully to topic {} at offset {} in partition {}",
						result.getRecordMetadata().topic(), result.getRecordMetadata().offset(),
						result.getRecordMetadata().partition());
			}
		});
	}
}