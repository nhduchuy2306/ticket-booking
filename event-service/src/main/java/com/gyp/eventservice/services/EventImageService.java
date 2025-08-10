package com.gyp.eventservice.services;

import java.util.List;

import com.gyp.eventservice.dtos.eventimage.EventImageRequestDto;
import com.gyp.eventservice.dtos.eventimage.EventImageResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface EventImageService {
	EventImageResponseDto createEventImageDto(EventImageRequestDto dto, MultipartFile imageFile);

	EventImageResponseDto getEventImageById(String id);

	EventImageResponseDto updateEventImage(String id, EventImageRequestDto dto, MultipartFile imageFile);

	void deleteEventImage(String id);

	List<EventImageResponseDto> getEventImages();
}
