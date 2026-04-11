package com.gyp.eventservice.messages.consumers;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.gyp.common.annotations.KafkaComponent;
import com.gyp.common.converters.Serialization;
import com.gyp.common.kafkatopics.TopicConstants;
import com.gyp.common.models.PaymentOutcomeEM;
import com.gyp.eventservice.entities.EventEntity;
import com.gyp.eventservice.entities.SeatInventoryEntity;
import com.gyp.eventservice.messages.producers.GenerateTicketPdfAndSendEmailProducer;
import com.gyp.eventservice.repositories.EventRepository;
import com.gyp.eventservice.repositories.SeatInventoryRepository;
import com.gyp.eventservice.services.SeatInventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.kafka.annotation.KafkaListener;

@Slf4j
@KafkaComponent
@RequiredArgsConstructor
public class PaymentSuccessConsumer {
	private final SeatInventoryService seatInventoryService;
	private final EventRepository eventRepository;
	private final SeatInventoryRepository seatInventoryRepository;
	private final GenerateTicketPdfAndSendEmailProducer generateTicketPdfAndSendEmailProducer;

	@KafkaListener(topics = TopicConstants.PAYMENT_SUCCESS_EVENT)
	public void handlePaymentSuccess(String message) {
		try {
			PaymentOutcomeEM paymentOutcomeEM = Serialization.deserializeFromString(message, PaymentOutcomeEM.class);
			EventEntity eventEntity = eventRepository.findById(paymentOutcomeEM.getEventId())
					.orElseThrow(() -> new IllegalArgumentException(
							"Event not found with id: " + paymentOutcomeEM.getEventId()));
			var confirmedSeatKeys = seatInventoryService.confirmSeatsForOrder(paymentOutcomeEM.getEventId(),
					paymentOutcomeEM.getOrderId());
			if(CollectionUtils.isEmpty(confirmedSeatKeys)) {
				log.info("No active holds found for payment success order ID: {}", paymentOutcomeEM.getOrderId());
				return;
			}
			List<SeatInventoryEntity> confirmedSeats = loadConfirmedSeats(paymentOutcomeEM.getEventId(),
					confirmedSeatKeys);
			if(confirmedSeats.size() != confirmedSeatKeys.size()) {
				throw new IllegalStateException("Failed to load full seat metadata for confirmed order: "
						+ paymentOutcomeEM.getOrderId());
			}
			confirmedSeats.forEach(seatInventoryEntity -> generateTicketPdfAndSendEmailProducer.sendGenerateTicketPdf(
					eventEntity, seatInventoryEntity, paymentOutcomeEM.getCustomerEmail(),
					paymentOutcomeEM.getIdempotencyKey()));
			generateTicketPdfAndSendEmailProducer.sendEmail(eventEntity, confirmedSeats,
					paymentOutcomeEM.getCustomerEmail(), paymentOutcomeEM.getIdempotencyKey());
			log.info("Payment success processed for order ID: {}", paymentOutcomeEM.getOrderId());
		} catch(Exception e) {
			log.error("Failed to process payment success event", e);
			throw new RuntimeException("Failed to process payment success event", e);
		}
	}

	private List<SeatInventoryEntity> loadConfirmedSeats(String eventId, List<String> confirmedSeatKeys) {
		List<SeatInventoryEntity> seatEntities = seatInventoryRepository.findByEventIdAndSeatKeyIn(eventId, confirmedSeatKeys);
		Map<String, SeatInventoryEntity> seatByKey = new LinkedHashMap<>();
		for(SeatInventoryEntity seatInventoryEntity : seatEntities) {
			seatByKey.put(seatInventoryEntity.getSeatKey(), seatInventoryEntity);
		}
		return confirmedSeatKeys.stream()
				.map(seatByKey::get)
				.filter(java.util.Objects::nonNull)
				.toList();
	}
}