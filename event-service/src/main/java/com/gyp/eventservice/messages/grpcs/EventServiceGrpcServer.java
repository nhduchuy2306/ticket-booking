package com.gyp.eventservice.messages.grpcs;

import com.google.protobuf.Empty;
import com.gyp.eventservice.grpc.event.EventResponse;
import com.gyp.eventservice.grpc.event.EventResponseList;
import com.gyp.eventservice.grpc.event.EventServiceGrpc;
import com.gyp.eventservice.grpc.event.EventStatus;
import com.gyp.eventservice.repositories.EventRepository;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class EventServiceGrpcServer extends EventServiceGrpc.EventServiceImplBase {
	private final EventRepository eventRepository;

	@Override
	public void getAllEvents(Empty request, StreamObserver<EventResponseList> responseObserver) {
		var events = eventRepository.findAll();
		var eventResponses = events.stream().map((item) ->
				EventResponse.newBuilder()
						.setId(item.getId())
						.setName(item.getName())
						.setDescription(item.getDescription())
						.setStatus(EventStatus.valueOf(item.getStatus().name()))
						.build()).toList();
		var response = EventResponseList.newBuilder()
				.addAllEvents(eventResponses)
				.build();

		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}
}
