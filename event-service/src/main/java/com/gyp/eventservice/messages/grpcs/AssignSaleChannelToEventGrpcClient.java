package com.gyp.eventservice.messages.grpcs;

import java.util.List;

import com.gyp.salechannelservice.grpc.salechannel.SaleChannelByEventIdRequest;
import com.gyp.salechannelservice.grpc.salechannel.SaleChannelByEventIdResponse;
import com.gyp.salechannelservice.grpc.salechannel.SaleChannelByEventIdResponseList;
import com.gyp.salechannelservice.grpc.salechannel.SaleChannelServiceGrpc;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AssignSaleChannelToEventGrpcClient {
	private final SaleChannelServiceGrpc.SaleChannelServiceBlockingStub saleChannelServiceBlockingStub;

	public List<SaleChannelByEventIdResponse> getSaleChannelsByEventId(String eventId) {
		try {
			SaleChannelByEventIdResponseList response = saleChannelServiceBlockingStub
					.getSaleChannelByEventId(SaleChannelByEventIdRequest.newBuilder().setEventId(eventId).build());
			return response.getSaleChannelsList();
		} catch(Exception e) {
			throw new RuntimeException("Failed to fetch sale channels for event ID: " + eventId, e);
		}
	}
}
