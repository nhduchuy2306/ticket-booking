package com.gyp.ticketservice.messages.grpcs;

import java.util.List;

import com.gyp.ticketservice.dtos.tickettype.TicketTypeResponseDto;
import com.gyp.tickettype.grpc.tickettype.TicketTypeServiceGrpc;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketTypeServiceGrpcClient {
	private final TicketTypeServiceGrpc.TicketTypeServiceBlockingStub ticketTypeServiceBlockingStub;

}
