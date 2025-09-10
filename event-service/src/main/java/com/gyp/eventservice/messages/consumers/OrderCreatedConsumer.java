package com.gyp.eventservice.messages.consumers;

import java.util.List;
import java.util.Objects;

import com.gyp.common.annotations.KafkaComponent;
import com.gyp.common.converters.Serialization;
import com.gyp.common.kafkatopics.TopicConstants;
import com.gyp.common.models.OrderCreatedEM;
import com.gyp.eventservice.dtos.seatmap.SeatConfig;
import com.gyp.eventservice.entities.EventEntity;
import com.gyp.eventservice.entities.SeatMapEntity;
import com.gyp.eventservice.entities.VenueMapEntity;
import com.gyp.eventservice.messages.producers.GenerateTicketPdfAndSendEmailProducer;
import com.gyp.eventservice.repositories.EventRepository;
import com.gyp.eventservice.repositories.SeatMapRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.kafka.annotation.KafkaListener;

@Slf4j
@KafkaComponent
@RequiredArgsConstructor
public class OrderCreatedConsumer {
	private final EventRepository eventRepository;
	private final SeatMapRepository seatMapRepository;
	private final GenerateTicketPdfAndSendEmailProducer generateTicketPdfAndSendEmailProducer;

	@KafkaListener(topics = TopicConstants.ORDER_CREATED_EVENT)
	public void updateEvent(String orderCreatedEMString) {
		try {
			OrderCreatedEM orderCreatedEM = Serialization.deserializeFromString(
					orderCreatedEMString, OrderCreatedEM.class);
			if(StringUtils.isNotEmpty(orderCreatedEM.getEventId())) {
				var eventOptional = eventRepository.findById(orderCreatedEM.getEventId());
				if(eventOptional.isPresent()) {
					EventEntity event = eventOptional.get();
					// Mark seats as booked
					VenueMapEntity venueMap = event.getVenueMapEntity();
					if(Objects.nonNull(venueMap)) {
						SeatMapEntity seatMapEntity = venueMap.getSeatMapEntity();
						String seatConfigRaw = seatMapEntity.getSeatConfigRaw();
						SeatConfig seatConfig = Serialization.deserializeFromString(seatConfigRaw, SeatConfig.class);
						Pair<SeatConfig, List<String>> soldSeatsPair = seatConfig.soldSeats(
								orderCreatedEM.getSeatIds());
						seatMapEntity.setSeatConfigRaw(Serialization.serializeToString(soldSeatsPair.getLeft()));
						seatMapRepository.save(seatMapEntity);
						log.info("Updated seat map for event ID: {}", event.getId());

						if(CollectionUtils.isNotEmpty(soldSeatsPair.getRight())) {
							// Generate ticket PDF
							soldSeatsPair.getRight().forEach(seatId -> {
								generateTicketPdfAndSendEmailProducer.sendGenerateTicketPdf(event, seatId,
										orderCreatedEM.getCustomerEmail());
							});

							// Send email with tickets
							generateTicketPdfAndSendEmailProducer.sendEmail(event, soldSeatsPair.getRight(),
									orderCreatedEM.getCustomerEmail());
						}
					}
					log.info("Seats marked as booked for event ID: {}", event.getId());
				} else {
					log.warn("Event not found with ID: {}", orderCreatedEM.getEventId());
				}
			}
		} catch(Exception e) {
			log.error("Failed to process order created event", e);
			throw new RuntimeException("Failed to process order created event", e);
		}
	}
}
