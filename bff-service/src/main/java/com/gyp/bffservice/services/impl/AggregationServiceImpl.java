package com.gyp.bffservice.services.impl;

import java.util.List;

import com.gyp.bffservice.dtos.EventWithChannelsDto;
import com.gyp.bffservice.services.AggregationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AggregationServiceImpl implements AggregationService {
	private final WebClient.Builder webClientBuilder;

	@Override
	public Mono<EventWithChannelsDto> getEventWithSaleChannels(String eventId) {
		WebClient client = webClientBuilder.build();
		Mono<Object> eventMono = client.get()
				.uri("http://EVENT-SERVICE/events/{id}", eventId)
				.retrieve()
				.bodyToMono(Object.class);

		Mono<List<Object>> saleChannelsMono = client.get()
				.uri("http://SALECHANNEL-SERVICE/salechannels/event/{eventId}", eventId)
				.retrieve()
				.bodyToFlux(Object.class)
				.collectList();
		return Mono.zip(eventMono, saleChannelsMono, EventWithChannelsDto::new);
	}
}
