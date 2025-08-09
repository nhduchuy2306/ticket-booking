package com.gyp.eventservice.services.impl;

import java.util.List;

import com.gyp.eventservice.dtos.eventimage.EventImageRequestDto;
import com.gyp.eventservice.dtos.eventimage.EventImageResponseDto;
import com.gyp.eventservice.entities.EventImageEntity;
import com.gyp.eventservice.mappers.EventImageMapper;
import com.gyp.eventservice.repositories.EventImageRepository;
import com.gyp.eventservice.services.EventImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventImageServiceImpl implements EventImageService {
	private final EventImageRepository eventImageRepository;
	private final EventImageMapper eventImageMapper;

	@Override
	public List<EventImageResponseDto> getEventImages() {
		var eventImages = eventImageRepository.findAll();
		return eventImageMapper.toResponseDtoList(eventImages);
	}

	@Override
	public EventImageResponseDto createEventImageDto(EventImageRequestDto dto) {
		EventImageEntity eventImageEntity = eventImageMapper.toEntity(dto);
		eventImageEntity = eventImageRepository.save(eventImageEntity);
		return eventImageMapper.toResponseDto(eventImageEntity);
	}

	@Override
	public EventImageResponseDto getEventImageById(String id) {
		return eventImageRepository.findById(id)
				.map(eventImageMapper::toResponseDto)
				.orElse(null);
	}

	@Override
	public EventImageResponseDto updateEventImage(String id, EventImageRequestDto dto) {
		var existingEvent = eventImageRepository.findById(id);
		if(existingEvent.isEmpty()) {
			throw new IllegalArgumentException("Event image with id " + id + " does not exist.");
		}
		EventImageEntity eventImageEntity = existingEvent.get();
		eventImageMapper.updateEntityFromDto(dto, eventImageEntity);
		eventImageEntity = eventImageRepository.save(eventImageEntity);
		return eventImageMapper.toResponseDto(eventImageEntity);
	}

	@Override
	public void deleteEventImage(String id) {
		var existingEvent = eventImageRepository.findById(id);
		if(existingEvent.isEmpty()) {
			throw new IllegalArgumentException("Event image with id " + id + " does not exist.");
		}
		existingEvent.ifPresent(eventImageRepository::delete);
	}
}
