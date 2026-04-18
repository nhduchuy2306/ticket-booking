package com.gyp.eventservice.services;

import java.util.List;

import com.gyp.eventservice.dtos.event.EventResponseDto;
import com.gyp.eventservice.dtos.seatmap.SeatAvailability;

public interface EventCacheService {
	EventResponseDto getEvent(String eventId);

	void cacheEvent(EventResponseDto eventResponseDto);

	void evictEvent(String eventId);

	List<EventResponseDto> getEventsOnSale();

	void cacheEventsOnSale(List<EventResponseDto> events);

	void evictEventsOnSale();

	List<EventResponseDto> getComingEvents();

	void cacheComingEvents(List<EventResponseDto> events);

	void evictComingEvents();

	List<EventResponseDto> getActiveEvents(String organizationId);

	void cacheActiveEvents(String organizationId, List<EventResponseDto> events);

	void evictActiveEvents(String organizationId);

	List<SeatAvailability> getSeatAvailability(String eventId);

	void cacheSeatAvailability(String eventId, List<SeatAvailability> seats);

	void evictSeatAvailability(String eventId);

	void evictBookingLists(String organizationId);
}