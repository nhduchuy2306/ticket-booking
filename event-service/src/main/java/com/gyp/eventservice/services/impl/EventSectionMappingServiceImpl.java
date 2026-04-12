package com.gyp.eventservice.services.impl;

import java.util.List;

import com.gyp.eventservice.dtos.eventsectionmapping.EventSectionMappingRequestDto;
import com.gyp.eventservice.dtos.eventsectionmapping.EventSectionMappingResponseDto;
import com.gyp.eventservice.entities.EventSectionMappingEntity;
import com.gyp.eventservice.mappers.EventSectionMappingMapper;
import com.gyp.eventservice.repositories.EventSectionMappingRepository;
import com.gyp.eventservice.services.EventSectionMappingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EventSectionMappingServiceImpl implements EventSectionMappingService {
	private final EventSectionMappingRepository eventSectionMappingRepository;
	private final EventSectionMappingMapper eventSectionMappingMapper;

	@Transactional
	@Override
	public List<EventSectionMappingResponseDto> getAllEventSectionMappingsForEvent(String eventId) {
		var eventSectionMappings = eventSectionMappingRepository.findAllByEventEntityId(eventId);
		if(eventSectionMappings.isEmpty()) {
			return List.of();
		}
		return eventSectionMappingMapper.toResponseDtoList(eventSectionMappings);
	}

	@Transactional
	@Override
	public List<EventSectionMappingResponseDto> createEventSectionMappingsForEvent(
			List<EventSectionMappingRequestDto> eventSectionMappingRequestDto) {
		return eventSectionMappingRequestDto.stream()
				.map(requestDto -> {
					var existMapping = eventSectionMappingRepository.findAllByEventEntityId(requestDto.getEventId());
					if(checkEventSectionMapping(requestDto, existMapping)) {
						return null;
					}
					var entity = eventSectionMappingMapper.toEntity(requestDto);
					var savedEntity = eventSectionMappingRepository.save(entity);
					return eventSectionMappingMapper.toResponseDto(savedEntity);
				})
				.toList();
	}

	@Transactional
	@Override
	public List<EventSectionMappingResponseDto> updateEventSectionMappingsForEvent(
			List<EventSectionMappingRequestDto> eventSectionMappingRequestDto) {
		return eventSectionMappingRequestDto.stream()
				.map(requestDto -> {
					var existingMapping = eventSectionMappingRepository.findById(requestDto.getId()).orElseThrow(
							() -> new RuntimeException("EventSectionMapping not found with id: " + requestDto.getId()));
					eventSectionMappingMapper.updateEntityFromDto(requestDto, existingMapping);
					return eventSectionMappingMapper.toResponseDto(existingMapping);
				})
				.toList();
	}

	private boolean checkEventSectionMapping(EventSectionMappingRequestDto eventSectionMappingRequestDto,
			List<EventSectionMappingEntity> entities) {
		return entities.stream().anyMatch(entity ->
				entity.getEventEntity().getId().equals(eventSectionMappingRequestDto.getEventId())
						&& entity.getTicketTypeEntity().getId().equals(eventSectionMappingRequestDto.getTicketTypeId())
						&& entity.getSeatMapEntity().getId().equals(eventSectionMappingRequestDto.getSeatMapId())
						&& entity.getSectionId().equals(eventSectionMappingRequestDto.getSectionId()));
	}
}
