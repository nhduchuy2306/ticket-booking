package com.gyp.eventservice.messages.grpcs;

import com.gyp.eventservice.repositories.EventRepository;
import com.gyp.eventservice.repositories.SeatMapRepository;
import com.gyp.seatmapservice.grpc.seatmap.SeatMapRequest;
import com.gyp.seatmapservice.grpc.seatmap.SeatMapResponse;
import com.gyp.seatmapservice.grpc.seatmap.SeatMapServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class SeatMapServiceGrpcServer extends SeatMapServiceGrpc.SeatMapServiceImplBase {
	private final SeatMapRepository seatMapRepository;
	private final EventRepository eventRepository;

	@Override
	public void getSeatMap(SeatMapRequest request, StreamObserver<SeatMapResponse> responseObserver) {
		var event = eventRepository.findById(request.getEventId());
		if(event.isPresent()) {
			var venueMap = event.get().getVenueMapEntity();
			if(venueMap != null) {
				String seatMapId = venueMap.getSeatMapEntity().getId();
				var seatMapEntity = seatMapRepository.findById(seatMapId);

				if(seatMapEntity.isPresent()) {
					var seatMap = seatMapEntity.get();
					var response = SeatMapResponse.newBuilder()
							.setId(seatMap.getId())
							.setName(seatMap.getName())
							.setVenueType(seatMap.getVenueType())
							.setOrganizationId(seatMap.getOrganizationId())
							.setSeatConfig(seatMap.getSeatConfigRaw())
							.setStageConfig(seatMap.getStageConfigRaw())
							.build();
					responseObserver.onNext(response);
					responseObserver.onCompleted();
				} else {
					responseObserver.onError(
							new RuntimeException("Seat map not found for event: " + request.getEventId()));
				}
			}
		}
	}
}
