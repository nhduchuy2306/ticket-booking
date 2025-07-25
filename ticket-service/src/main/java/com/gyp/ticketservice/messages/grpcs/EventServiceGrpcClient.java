package com.gyp.ticketservice.messages.grpcs;

import java.util.List;

import com.google.protobuf.Empty;
import com.gyp.eventservice.grpc.event.EventResponse;
import com.gyp.eventservice.grpc.event.EventResponseList;
import com.gyp.eventservice.grpc.event.EventServiceGrpc;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventServiceGrpcClient {
	private final EventServiceGrpc.EventServiceBlockingStub eventServiceBlockingStub;

	public List<EventResponse> getAllEvents() {
		try {
			EventResponseList response = eventServiceBlockingStub.getAllEvents(Empty.getDefaultInstance());
			return response.getEventsList();
		} catch(Exception e) {
			throw new RuntimeException("Failed to fetch events from EventService", e);
		}
	}
}
