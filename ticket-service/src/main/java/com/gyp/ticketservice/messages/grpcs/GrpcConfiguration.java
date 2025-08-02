package com.gyp.ticketservice.messages.grpcs;

import com.gyp.eventservice.grpc.event.EventServiceGrpc;
import com.gyp.seatmapservice.grpc.seatmap.SeatMapServiceGrpc;
import com.gyp.tickettype.grpc.tickettype.TicketTypeServiceGrpc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GrpcChannelFactory;

@Configuration
public class GrpcConfiguration {
	@Bean
	public EventServiceGrpc.EventServiceBlockingStub eventServiceBlockingStub(GrpcChannelFactory channelFactory) {
		return EventServiceGrpc.newBlockingStub(channelFactory.createChannel("event-service"));
	}

	@Bean
	public SeatMapServiceGrpc.SeatMapServiceBlockingStub seatMapServiceBlockingStub(GrpcChannelFactory channelFactory) {
		return SeatMapServiceGrpc.newBlockingStub(channelFactory.createChannel("seatmap-service"));
	}

	@Bean
	public TicketTypeServiceGrpc.TicketTypeServiceBlockingStub ticketTypeServiceBlockingStub(
			GrpcChannelFactory channelFactory) {
		return TicketTypeServiceGrpc.newBlockingStub(channelFactory.createChannel("tickettype-service"));
	}
}
