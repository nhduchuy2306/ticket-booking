package com.gyp.eventservice.messages.grpcs;

import com.gyp.salechannelservice.grpc.salechannel.SaleChannelServiceGrpc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GrpcChannelFactory;

@Configuration
public class GrpcConfiguration {
	@Bean
	public SaleChannelServiceGrpc.SaleChannelServiceBlockingStub saleChannelServiceBlockingStub(
			GrpcChannelFactory channelFactory) {
		return SaleChannelServiceGrpc.newBlockingStub(channelFactory.createChannel("salechannel-service"));
	}
}
