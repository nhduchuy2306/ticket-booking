package com.gyp.eventservice.services;

import java.time.LocalDateTime;
import java.util.List;

import com.gyp.common.dtos.pagination.PaginatedDto;
import com.gyp.common.exceptions.ResourceNotFoundException;
import com.gyp.common.intefaces.Validatable;
import com.gyp.eventservice.dtos.event.EventRequestDto;
import com.gyp.eventservice.dtos.event.EventResponseDto;
import com.gyp.eventservice.services.criteria.EventSearchCriteria;
import org.springframework.web.multipart.MultipartFile;

public interface EventService extends Validatable {
	List<EventResponseDto> getAllEvents(EventSearchCriteria criteria, PaginatedDto pagination);

	EventResponseDto getEventById(String id) throws ResourceNotFoundException;

	EventResponseDto createEvent(EventRequestDto request);

	EventResponseDto createEvent(EventRequestDto request, MultipartFile file);

	EventResponseDto updateEvent(String eventId, EventRequestDto request) throws ResourceNotFoundException;

	EventResponseDto updateEvent(String eventId, EventRequestDto request, MultipartFile file)
			throws ResourceNotFoundException;

	EventResponseDto publishEvent(String eventId);

	EventResponseDto cancelEvent(String eventId);

	List<EventResponseDto> searchEvents(EventSearchCriteria criteria);

	EventResponseDto getEventDetails(String eventId);

	void deleteEvent(String id) throws ResourceNotFoundException;

	List<EventResponseDto> getAllActiveEvents();

	List<EventResponseDto> getAllEventsOnSale();

	List<EventResponseDto> getAllComingEvents();

	default List<EventResponseDto> getEventsCreatedSince(LocalDateTime since) {
		return List.of();
	}

	default List<EventResponseDto> getTomorrowEvents() {
		return List.of();
	}
}
