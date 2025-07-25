package com.gyp.ticketservice.messages.grpcs;

import com.gyp.eventservice.grpc.event.EventServiceGrpc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GrpcChannelFactory;

@Configuration
public class GrpcConfiguration {
	@Bean
	public EventServiceGrpc.EventServiceBlockingStub eventServiceBlockingStub(GrpcChannelFactory channelFactory) {
		return EventServiceGrpc.newBlockingStub(channelFactory.createChannel("event-service"));
	}
}
