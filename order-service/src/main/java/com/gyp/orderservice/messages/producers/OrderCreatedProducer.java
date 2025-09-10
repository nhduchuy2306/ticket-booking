package com.gyp.orderservice.messages.producers;

import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gyp.common.annotations.KafkaComponent;
import com.gyp.common.converters.Serialization;
import com.gyp.common.kafkatopics.TopicConstants;
import com.gyp.common.models.OrderCreatedEM;
import com.gyp.orderservice.entities.OrderEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

@Slf4j
@KafkaComponent
@RequiredArgsConstructor
public class OrderCreatedProducer {
	private final KafkaTemplate<String, String> kafkaTemplate;

	public void sendOrderCreatedEvent(OrderEntity orderEntity) {
		try {
			OrderCreatedEM orderCreatedEM = OrderCreatedEM.builder()
					.orderId(orderEntity.getId())
					.customerEmail(orderEntity.getCustomerEmail())
					.eventId(orderEntity.getEventId())
					.totalAmount(orderEntity.getTotalAmount())
					.items(orderEntity.getOrderDetailEntityList().stream()
							.map(detail -> OrderCreatedEM.OrderItem.builder()
									.seatId(detail.getSeatId())
									.quantity(detail.getQuantity())
									.price(detail.getPrice())
									.orderId(orderEntity.getId())
									.build())
							.toList())
					.build();
			String orderCreatedString = Serialization.serializeToString(orderCreatedEM);
			CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(
					TopicConstants.ORDER_CREATED_EVENT, orderCreatedString);
			future.whenComplete((result, throwable) -> {
				if(throwable != null) {
					// Log the error
					log.error("Failed to send order created event: {}", throwable.getMessage());
				} else {
					// Log success
					log.info("Order created event sent successfully to topic {} at offset {} in partition {}",
							result.getRecordMetadata().topic(), result.getRecordMetadata().offset(),
							result.getRecordMetadata().partition());
				}
			});
		} catch(JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}
