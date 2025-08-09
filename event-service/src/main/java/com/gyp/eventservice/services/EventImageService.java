package com.gyp.eventservice.services;

import java.util.List;

import com.gyp.eventservice.dtos.eventimage.EventImageRequestDto;
import com.gyp.eventservice.dtos.eventimage.EventImageResponseDto;

public interface EventImageService {
	EventImageResponseDto createEventImageDto(EventImageRequestDto dto);

	EventImageResponseDto getEventImageById(String id);

	EventImageResponseDto updateEventImage(String id, EventImageRequestDto dto);

	void deleteEventImage(String id);

	List<EventImageResponseDto> getEventImages();
}
