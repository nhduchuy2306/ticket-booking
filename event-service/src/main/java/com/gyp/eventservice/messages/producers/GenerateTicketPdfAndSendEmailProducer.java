package com.gyp.eventservice.messages.producers;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.Collections;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gyp.common.annotations.KafkaComponent;
import com.gyp.common.converters.Serialization;
import com.gyp.common.kafkatopics.TopicConstants;
import com.gyp.common.models.EventGenerationPdfEM;
import com.gyp.eventservice.entities.EventEntity;
import com.gyp.eventservice.entities.SeatEntity;
import com.gyp.eventservice.entities.VenueEntity;
import com.gyp.eventservice.entities.VenueMapEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

@Slf4j
@KafkaComponent
@RequiredArgsConstructor
public class GenerateTicketPdfAndSendEmailProducer {
	private final KafkaTemplate<String, String> kafkaTemplate;

	public void sendGenerateTicketPdf(EventEntity eventEntity, SeatEntity seatEntity, String customerEmail,
			String idempotencyKey) {
		try {
			EventGenerationPdfEM eventGenerationPdfEM = createEventGenerationPdfEM(eventEntity, seatEntity, customerEmail,
					idempotencyKey);
			String eventGenerationPdfEMString = Serialization.serializeToString(eventGenerationPdfEM);
			CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(
					TopicConstants.GENERATE_TICKET_PDF_EVENT, eventGenerationPdfEMString);
			future.whenComplete((result, throwable) -> {
				if(throwable != null) {
					log.error("Failed to send GenerateTicketPdfAndSendEmail event for event ID {}: {}",
							eventEntity.getId(), throwable.getMessage());
				} else {
					log.info(
							"GenerateTicketPdfAndSendEmail event for event ID {} sent successfully to topic {} at offset {} in partition {}",
							eventEntity.getId(),
							result.getRecordMetadata().topic(),
							result.getRecordMetadata().offset(),
							result.getRecordMetadata().partition());
				}
			});
		} catch(JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	public void sendEmail(EventEntity eventEntity, List<SeatEntity> seatEntities, String customerEmail,
			String idempotencyKey) {
		try {
			EventGenerationPdfEM eventGenerationPdfEM = createEventGenerationPdfEM(eventEntity, seatEntities, customerEmail,
					idempotencyKey);
			String eventGenerationPdfEMString = Serialization.serializeToString(eventGenerationPdfEM);
			CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(
					TopicConstants.SEND_EMAIL_EVENT, eventGenerationPdfEMString);
			future.whenComplete((result, throwable) -> {
				if(throwable != null) {
					log.error("Failed to send email event for event ID {}: {}",
							eventEntity.getId(), throwable.getMessage());
				} else {
					log.info("Email event for event ID {} sent successfully to topic {} at offset {} in partition {}",
							eventEntity.getId(),
							result.getRecordMetadata().topic(),
							result.getRecordMetadata().offset(),
							result.getRecordMetadata().partition());
				}
			});
		} catch(JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	private EventGenerationPdfEM createEventGenerationPdfEM(EventEntity eventEntity, SeatEntity seatEntity,
			String customerEmail, String idempotencyKey) {
		VenueEntity venueEntity = checkVenueAndSeatMap(eventEntity);
		return EventGenerationPdfEM.builder()
				.id(eventEntity.getId())
				.name(eventEntity.getName())
				.orderId(idempotencyKey)
				.idempotencyKey(idempotencyKey)
				.description(eventEntity.getDescription())
				.status(eventEntity.getStatus())
				.seatId(seatEntity.getSeatKey())
				.seatLabel(seatEntity.getSeatLabel())
				.sectionId(seatEntity.getSectionId())
				.rowId(seatEntity.getRowId())
				.ticketTypeId(seatEntity.getTicketTypeId())
				.seatIds(Collections.singletonList(seatEntity.getSeatKey()))
				.seatLabels(Collections.singletonList(seatEntity.getSeatLabel()))
				.sectionIds(Collections.singletonList(seatEntity.getSectionId()))
				.rowIds(Collections.singletonList(seatEntity.getRowId()))
				.ticketTypeIds(Collections.singletonList(seatEntity.getTicketTypeId()))
				.logoUrl(eventEntity.getLogoUrl())
				.venueAddress(venueEntity != null ? venueEntity.getAddress() : null)
				.customerEmail(customerEmail)
				.startTime(eventEntity.getTime().getStartTime())
				.endTime(eventEntity.getTime().getEndTime())
				.doorOpenTime(eventEntity.getTime().getDoorOpenTime())
				.doorCloseTime(eventEntity.getTime().getDoorCloseTime())
				.build();
	}

	private EventGenerationPdfEM createEventGenerationPdfEM(EventEntity eventEntity, List<SeatEntity> seatEntities,
			String customerEmail, String idempotencyKey) {
		VenueEntity venueEntity = checkVenueAndSeatMap(eventEntity);
		List<String> seatIds = seatEntities.stream().map(SeatEntity::getSeatKey).toList();
		List<String> seatLabels = seatEntities.stream().map(SeatEntity::getSeatLabel).toList();
		List<String> sectionIds = seatEntities.stream().map(SeatEntity::getSectionId).toList();
		List<String> rowIds = seatEntities.stream().map(SeatEntity::getRowId).toList();
		List<String> ticketTypeIds = seatEntities.stream().map(SeatEntity::getTicketTypeId).toList();
		return EventGenerationPdfEM.builder()
				.id(eventEntity.getId())
				.name(eventEntity.getName())
				.orderId(idempotencyKey)
				.idempotencyKey(idempotencyKey)
				.description(eventEntity.getDescription())
				.status(eventEntity.getStatus())
				.seatIds(seatIds)
				.seatLabels(seatLabels)
				.sectionIds(sectionIds)
				.rowIds(rowIds)
				.ticketTypeIds(ticketTypeIds)
				.logoUrl(eventEntity.getLogoUrl())
				.venueAddress(venueEntity != null ? venueEntity.getAddress() : null)
				.customerEmail(customerEmail)
				.startTime(eventEntity.getTime().getStartTime())
				.endTime(eventEntity.getTime().getEndTime())
				.doorOpenTime(eventEntity.getTime().getDoorOpenTime())
				.doorCloseTime(eventEntity.getTime().getDoorCloseTime())
				.build();
	}

	private VenueEntity checkVenueAndSeatMap(EventEntity eventEntity) {
		VenueMapEntity venueMapEntity = eventEntity.getVenueMapEntity();
		if(venueMapEntity == null) {
			log.warn("Venue map entity is null for event ID: {}", eventEntity.getId());
			return null;
		}
		VenueEntity venueEntity = venueMapEntity.getVenueEntity();
		if(venueEntity == null) {
			log.warn("Venue entity is null for venue map ID: {}", venueMapEntity.getId());
			return null;
		}
		return venueEntity;
	}
}
