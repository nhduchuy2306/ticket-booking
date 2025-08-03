package com.gyp.eventservice.services;

import java.util.List;

import com.gyp.common.dtos.pagination.PaginatedDto;
import com.gyp.common.exceptions.ResourceNotFoundException;
import com.gyp.common.intefaces.Validatable;
import com.gyp.common.models.EventEventModel;
import com.gyp.eventservice.dtos.event.EventRequestDto;
import com.gyp.eventservice.dtos.event.EventResponseDto;
import com.gyp.eventservice.services.criteria.EventSearchCriteria;

public interface EventService extends Validatable {
	List<EventEventModel> getListEventModel();

	List<EventResponseDto> getAllEvents(EventSearchCriteria criteria, PaginatedDto pagination);

	EventResponseDto getEventById(String id) throws ResourceNotFoundException;

	EventResponseDto createEvent(EventRequestDto request);

	EventResponseDto updateEvent(String eventId, EventRequestDto request) throws ResourceNotFoundException;

	EventResponseDto publishEvent(String eventId);

	EventResponseDto cancelEvent(String eventId);

	List<EventResponseDto> searchEvents(EventSearchCriteria criteria);

	EventResponseDto getEventDetails(String eventId);

	void deleteEvent(String id) throws ResourceNotFoundException;

	List<EventResponseDto> getAllActiveEvents();
}
