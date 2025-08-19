package com.gyp.salechannelservice.messages.grpcs;

import com.gyp.salechannelservice.grpc.salechannel.SaleChannelByEventIdRequest;
import com.gyp.salechannelservice.grpc.salechannel.SaleChannelByEventIdResponse;
import com.gyp.salechannelservice.grpc.salechannel.SaleChannelByEventIdResponseList;
import com.gyp.salechannelservice.grpc.salechannel.SaleChannelServiceGrpc.SaleChannelServiceImplBase;
import com.gyp.salechannelservice.repositories.SaleChannelEventRepository;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class AssignSaleChannelToEventGrpcServer extends SaleChannelServiceImplBase {
	private final SaleChannelEventRepository saleChannelEventRepository;

	@Override
	public void getSaleChannelByEventId(SaleChannelByEventIdRequest request,
			StreamObserver<SaleChannelByEventIdResponseList> responseObserver) {
		var saleChannels = saleChannelEventRepository.findByEventId(request.getEventId());
		var saleChannelResponses = saleChannels.stream().map((item) ->
				SaleChannelByEventIdResponse.newBuilder()
						.setId(item.getSaleChannelEntity().getId())
						.setName(item.getSaleChannelEntity().getName())
						.build()).toList();

		var response = SaleChannelByEventIdResponseList.newBuilder()
				.addAllSaleChannels(saleChannelResponses)
				.build();

		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}
}
