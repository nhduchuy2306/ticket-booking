package com.gyp.eventservice.messages.producers;

import java.util.concurrent.CompletableFuture;

import com.gyp.common.converters.Serialization;
import com.gyp.common.kafkatopics.EventServiceTopic;
import com.gyp.eventservice.mappers.SeatMapMapper;
import com.gyp.eventservice.repositories.EventRepository;
import com.gyp.eventservice.repositories.SeatMapRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GenerateSeatMapTicketProducer {
	private final KafkaTemplate<String, String> kafkaTemplate;
	private final SeatMapRepository seatMapRepository;
	private final EventRepository eventRepository;
	private final SeatMapMapper seatMapMapper;

	public void generateSeatMapTicket(String eventId) {
		try {
			var event = eventRepository.findById(eventId);
			if(event.isPresent()) {
				var currentEvent = event.get();
				var venueMap = currentEvent.getVenueMapEntity();
				if(venueMap != null) {
					String seatMapId = venueMap.getSeatMapEntity().getId();
					var seatMapEntity = seatMapRepository.findById(seatMapId);
					if(seatMapEntity.isPresent()) {
						var seatMap = seatMapEntity.get();
						var seatMapTicket = seatMapMapper.toSeatMapTicketEM(seatMap);
						seatMapTicket.setEventId(eventId);
						seatMapTicket.setEventName(currentEvent.getName());
						seatMapTicket.setEventDateTime(currentEvent.getTime().getStartTime());
						seatMapTicket.setVenueType(seatMap.getVenueType());
						seatMapTicket.setOrganizationId(seatMap.getOrganizationId());

						String dataString = Serialization.serializeToString(seatMapTicket);
						CompletableFuture<SendResult<String, String>> future =
								kafkaTemplate.send(EventServiceTopic.GENERATE_SEAT_MAP_COMMAND, dataString);
						future.whenComplete((result, throwable) -> {
							if(throwable != null) {
								log.error("Failed to send message to topic {}: {}",
										EventServiceTopic.GENERATE_SEAT_MAP_COMMAND,
										throwable.getMessage());
							} else {
								log.info("Message sent successfully to topic {} at offset {} in partition {}",
										result.getRecordMetadata().topic(),
										result.getRecordMetadata().offset(),
										result.getRecordMetadata().partition());
								log.info("Sent sync SeatMap data: {}", dataString);
							}
						});
					}
				}
			}
		} catch(Exception e) {
			log.error("Serialization failed", e);
			throw new RuntimeException("Failed to sync SeatMap data", e);
		}
	}
}
