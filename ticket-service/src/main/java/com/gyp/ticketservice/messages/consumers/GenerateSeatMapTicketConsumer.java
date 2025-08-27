package com.gyp.ticketservice.messages.consumers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gyp.common.converters.Serialization;
import com.gyp.common.kafkatopics.EventServiceTopic;
import com.gyp.common.models.SeatMapTicketEM;
import com.gyp.ticketservice.dtos.seatmap.SeatMapDto;
import com.gyp.ticketservice.mappers.SeatMapMapper;
import com.gyp.ticketservice.services.TicketGenerationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GenerateSeatMapTicketConsumer {
	private final TicketGenerationService ticketGenerationService;
	private final SeatMapMapper seatMapMapper;

	@KafkaListener(topics = EventServiceTopic.GENERATE_SEAT_MAP_COMMAND)
	public void generateSeatMapTicket(String seatMapTicketString) {
		log.info("Received seat map sync event: {}", seatMapTicketString);
		log.info("Seat maps synced successfully");

		try {
			var seatMapTicketEM = Serialization.deserializeFromString(seatMapTicketString, SeatMapTicketEM.class);
			SeatMapDto seatMapDto = seatMapMapper.toDto(seatMapTicketEM);
			ticketGenerationService.generateTicketBaseOnEventConfiguration(seatMapDto, seatMapTicketEM.getEventId());
		} catch(JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}
