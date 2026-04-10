package com.gyp.eventservice.services;

import java.util.List;

import com.gyp.common.models.SeatMapEventModel;
import com.gyp.eventservice.dtos.seatmap.Seat;
import com.gyp.eventservice.dtos.seatmap.SeatConfig;
import com.gyp.eventservice.dtos.seatmap.SeatMapRequestDto;
import com.gyp.eventservice.dtos.seatmap.SeatMapResponseDto;
import com.gyp.eventservice.dtos.seatmap.SeatStatus;
import com.gyp.eventservice.dtos.seatmap.SeatWithScore;
import com.gyp.eventservice.dtos.seatmap.VenueMap;

public interface SeatMapService {
	String convertSeatMapJson(String content);

	VenueMap createTheaterSeatMap(String name, int rows, int seatsPerRow);

	VenueMap createStadiumConcertSeatMap(String name, boolean includeField);

	VenueMap createConferenceSeatMap(String name, int numberOfTables, int seatsPerTable);

	VenueMap createArenaConcertSeatMap(String name);

	VenueMap createClubSeatMap(String name, int standingCapacity);

	List<SeatWithScore> findBestSeats(String venueMapId, int limit);

	List<Seat> findSeatsBySection(String venueMapId, String sectionId, SeatStatus status);

	SeatConfig parseSeatConfig(String seatConfigJson);

	List<SeatMapResponseDto> getAllSeatMaps();

	SeatMapResponseDto getSeatMapById(String seatMapId);

	SeatMapResponseDto updateSeatMap(String seatMapId, SeatMapRequestDto seatMapDto);

	SeatMapResponseDto createSeatMap(SeatMapRequestDto seatMapDto);

	void deleteSeatMap(String seatMapId);

	List<SeatMapEventModel> getListSeatMapModel();

	void generateSeatMapTicket(String eventId);
}
