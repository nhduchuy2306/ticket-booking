package com.gyp.ticketservice.messages.grpcs;

import java.util.List;

import com.gyp.seatmapservice.grpc.seatmap.SeatMapRequest;
import com.gyp.seatmapservice.grpc.seatmap.SeatMapResponse;
import com.gyp.seatmapservice.grpc.seatmap.SeatMapServiceGrpc;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SeatMapServiceGrpcClient {
	private final SeatMapServiceGrpc.SeatMapServiceBlockingStub seatMapServiceBlockingStub;

	public List<SeatMapResponse> getSeatMaps(SeatMapRequest request) {
		try {
			SeatMapResponse response = seatMapServiceBlockingStub.getSeatMap(request);
			return List.of(response);
		} catch(Exception e) {
			throw new RuntimeException("Failed to fetch seat map from SeatMapService", e);
		}
	}
}
