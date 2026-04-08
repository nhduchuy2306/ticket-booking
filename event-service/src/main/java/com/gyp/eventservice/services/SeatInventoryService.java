package com.gyp.eventservice.services;

import java.util.List;

public interface SeatInventoryService {
	void initializeSeatsForEvent(String eventId);

	List<String> reserveSeatsForOrder(String eventId, String holdToken, String userId, List<String> seatKeys);

	List<String> confirmSeatsForOrder(String eventId, String holdToken);

	List<String> releaseSeatsForOrder(String eventId, String holdToken);
}