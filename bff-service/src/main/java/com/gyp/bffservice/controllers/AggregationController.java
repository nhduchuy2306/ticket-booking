package com.gyp.bffservice.controllers;

import com.gyp.bffservice.dtos.EventWithChannelsDto;
import com.gyp.bffservice.services.AggregationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(AggregationController.AGGREGATION_CONTROLLER_PATH)
public class AggregationController {
	public static final String AGGREGATION_CONTROLLER_PATH = "/aggregations";
	private static final String EVENT_WITH_CHANNELS_PATH = "/events/{eventId}/with-channels";

	private final AggregationService aggregationService;

	@GetMapping(EVENT_WITH_CHANNELS_PATH)
	public Mono<EventWithChannelsDto> getEventWithSaleChannels(@PathVariable String eventId) {
		return aggregationService.getEventWithSaleChannels(eventId);
	}
}
