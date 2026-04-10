package com.gyp.eventservice.services;

import java.util.List;

import com.gyp.eventservice.dtos.seatmap.SeatAvailability;
import com.gyp.eventservice.dtos.seatmap.SeatHoldRequestDto;
import com.gyp.eventservice.dtos.seatmap.SeatHoldResponseDto;

public interface SeatInventoryService {
	void initializeSeatsForEvent(String eventId);

	List<SeatAvailability> getSeatAvailability(String eventId);

	SeatHoldResponseDto reserveSeats(SeatHoldRequestDto request);

	SeatHoldResponseDto confirmSeats(SeatHoldRequestDto request);

	SeatHoldResponseDto releaseSeats(SeatHoldRequestDto request);

	List<String> reserveSeatsForOrder(String eventId, String holdToken, String userId, List<String> seatKeys);

	List<String> confirmSeatsForOrder(String eventId, String holdToken);

	List<String> releaseSeatsForOrder(String eventId, String holdToken);
}