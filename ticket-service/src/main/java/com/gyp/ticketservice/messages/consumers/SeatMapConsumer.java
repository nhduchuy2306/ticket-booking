package com.gyp.ticketservice.messages.consumers;

import com.gyp.common.kafkatopics.EventServiceTopic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SeatMapConsumer {
	@KafkaListener(topics = EventServiceTopic.SEAT_MAP_SYNC)
	public void syncSeatMap(String seatMapResponseString) {
		log.info("Received seat map sync event: {}", seatMapResponseString);
		log.info("Seat maps synced successfully");
	}
}
