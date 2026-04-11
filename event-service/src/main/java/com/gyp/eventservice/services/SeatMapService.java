package com.gyp.eventservice.services;

import java.util.List;

import com.gyp.common.models.SeatMapEventModel;
import com.gyp.eventservice.dtos.seatmap.SeatConfig;
import com.gyp.eventservice.dtos.seatmap.SeatMapRequestDto;
import com.gyp.eventservice.dtos.seatmap.SeatMapResponseDto;

public interface SeatMapService {
	String convertSeatMapJson(String content);

	SeatConfig parseSeatConfig(String seatConfigJson);

	List<SeatMapResponseDto> getAllSeatMaps();

	SeatMapResponseDto getSeatMapById(String seatMapId);

	SeatMapResponseDto updateSeatMap(String seatMapId, SeatMapRequestDto seatMapDto);

	SeatMapResponseDto createSeatMap(SeatMapRequestDto seatMapDto);

	void deleteSeatMap(String seatMapId);

	List<SeatMapEventModel> getListSeatMapModel();

	void generateSeatMapTicket(String eventId);
}
