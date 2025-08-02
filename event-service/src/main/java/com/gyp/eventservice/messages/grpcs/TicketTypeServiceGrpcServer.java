package com.gyp.eventservice.messages.grpcs;

import com.gyp.eventservice.repositories.EventRepository;
import com.gyp.eventservice.repositories.TicketTypeRepository;
import com.gyp.tickettype.grpc.tickettype.TicketTypeRequest;
import com.gyp.tickettype.grpc.tickettype.TicketTypeResponse;
import com.gyp.tickettype.grpc.tickettype.TicketTypeResponseList;
import com.gyp.tickettype.grpc.tickettype.TicketTypeServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class TicketTypeServiceGrpcServer extends TicketTypeServiceGrpc.TicketTypeServiceImplBase {
	private final EventRepository eventRepository;
	private final TicketTypeRepository ticketTypeRepository;

	@Override
	public void getTicketType(TicketTypeRequest request, StreamObserver<TicketTypeResponseList> responseObserver) {
		var event = eventRepository.findById(request.getEventId());
		if(event.isPresent()) {
			var currentEvent = event.get();
			var ticketType = ticketTypeRepository.findAllByEventEntityId(currentEvent.getId());
			if(ticketType.isEmpty()) {
				responseObserver.onError(
						new RuntimeException("No ticket types found for event: " + request.getEventId()));
				return;
			}

			var response = ticketType.stream().map(item ->
					TicketTypeResponse.newBuilder()
							.setId(item.getId())
							.setName(item.getName())
							.setDescription(item.getDescription())
							.setEventId(currentEvent.getId())
							.setPrice(item.getPrice())
							.setDescription(item.getDescription())
							.setQuantityAvailable(item.getQuantityAvailable())
							.setStatus(item.getStatus().name())
							.setSaleStartDate(item.getSaleStartDate().toString())
							.setSaleEndDate(item.getSaleEndDate().toString())
							.build()).toList();

			var responseList = TicketTypeResponseList.newBuilder()
					.addAllTicketTypes(response)
					.build();

			responseObserver.onNext(responseList);
			responseObserver.onCompleted();
		}
		super.getTicketType(request, responseObserver);
	}
}
