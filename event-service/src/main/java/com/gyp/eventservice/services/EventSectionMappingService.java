package com.gyp.eventservice.services;

import java.util.List;

import com.gyp.eventservice.dtos.eventsectionmapping.EventSectionMappingRequestDto;
import com.gyp.eventservice.dtos.eventsectionmapping.EventSectionMappingResponseDto;

public interface EventSectionMappingService {
	List<EventSectionMappingResponseDto> getAllEventSectionMappingsForEvent(String eventId);

	List<EventSectionMappingResponseDto> createEventSectionMappingsForEvent(
			List<EventSectionMappingRequestDto> eventSectionMappingRequestDto);

	List<EventSectionMappingResponseDto> updateEventSectionMappingsForEvent(
			List<EventSectionMappingRequestDto> eventSectionMappingRequestDto);
}
