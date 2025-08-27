package com.gyp.bffservice.services;

import com.gyp.bffservice.dtos.EventWithChannelsDto;
import reactor.core.publisher.Mono;

public interface AggregationService {
	Mono<EventWithChannelsDto> getEventWithSaleChannels(String eventId);
}
