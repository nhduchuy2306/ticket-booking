package com.gyp.eventservice.messages.producers;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import jakarta.annotation.PostConstruct;

import com.gyp.common.converters.Serialization;
import com.gyp.common.kafkatopics.EventServiceTopic;
import com.gyp.common.models.SeatMapEventModel;
import com.gyp.eventservice.services.SeatMapService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SeatMapProducer {
	@Value("${app.sync-on-startup:false}")
	private boolean syncOnStartup;

	private final KafkaTemplate<String, String> kafkaTemplate;
	private final SeatMapService seatMapService;

	public void syncSeatMap() {
//		try {
//			List<SeatMapEventModel> seatMapEventModels = seatMapService.getListSeatMapModel();
//			String dataString = Serialization.serializeToString(seatMapEventModels);
//
//			CompletableFuture<SendResult<String, String>> future =
//					kafkaTemplate.send(EventServiceTopic.SEAT_MAP_SYNC, dataString);
//
//			future.whenComplete((result, throwable) -> {
//				if(throwable != null) {
//					log.error("Failed to send message to topic {}: {}", EventServiceTopic.SEAT_MAP_SYNC,
//							throwable.getMessage());
//				} else {
//					log.info("Message sent successfully to topic {} at offset {} in partition {}",
//							result.getRecordMetadata().topic(),
//							result.getRecordMetadata().offset(),
//							result.getRecordMetadata().partition());
//					log.info("Sent sync SeatMap data: {}", dataString);
//				}
//			});
//		} catch(Exception e) {
//			log.error("Serialization failed", e);
//			throw new RuntimeException("Failed to sync SeatMap data", e);
//		}
	}

	@PostConstruct
	public void init() {
		log.info("SeatMapProducer initialized, listening on topic: {}", EventServiceTopic.SEAT_MAP_SYNC);
		if(syncOnStartup) {
			syncSeatMap();
		}
	}

	@EventListener(ApplicationReadyEvent.class)
	public void onStartUp() {
		if(syncOnStartup) {
			syncSeatMap();
		}
	}
}
