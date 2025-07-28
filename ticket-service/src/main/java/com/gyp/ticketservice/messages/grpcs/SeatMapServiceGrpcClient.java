package com.gyp.ticketservice.messages.grpcs;

import com.gyp.seatmapservice.grpc.seatmap.SeatMapRequest;
import com.gyp.seatmapservice.grpc.seatmap.SeatMapResponse;
import com.gyp.seatmapservice.grpc.seatmap.SeatMapServiceGrpc;
import com.gyp.ticketservice.dtos.seatmap.SeatMapDto;
import com.gyp.ticketservice.mappers.SeatMapMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SeatMapServiceGrpcClient {
	private final SeatMapServiceGrpc.SeatMapServiceBlockingStub seatMapServiceBlockingStub;
	private final SeatMapMapper seatMapMapper;

	public SeatMapDto getSeatMap(SeatMapRequest request) {
		try {
			SeatMapResponse response = seatMapServiceBlockingStub.getSeatMap(request);
			return seatMapMapper.toDto(response);
		} catch(Exception e) {
			throw new RuntimeException("Failed to fetch seat map from SeatMapService", e);
		}
	}
}
