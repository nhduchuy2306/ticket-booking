package com.gyp.eventservice.services;

import java.util.List;

import com.gyp.common.models.EventEventModel;
import com.gyp.eventservice.exceptions.EventNotFoundException;
import com.gyp.eventservice.services.criteria.EventSearchCriteria;
import com.gyp.eventservice.dtos.event.EventRequestDto;
import com.gyp.eventservice.dtos.event.EventResponseDto;

public interface EventService {
	List<EventEventModel> getListEventModel();

	List<EventResponseDto> getAllEvents();

	EventResponseDto getEventById(String id) throws EventNotFoundException;

	EventResponseDto createEvent(EventRequestDto request);

	EventResponseDto updateEvent(String eventId, EventRequestDto request);

	EventResponseDto publishEvent(String eventId);

	EventResponseDto cancelEvent(String eventId);

	List<EventResponseDto> findEventsByOrganizer(String organizerId);

	List<EventResponseDto> searchEvents(EventSearchCriteria criteria);

	EventResponseDto getEventDetails(String eventId);
}
