package com.gyp.eventservice.services.impl;

import java.util.List;

import com.gyp.common.models.EventEventModel;
import com.gyp.eventservice.dtos.event.EventRequestDto;
import com.gyp.eventservice.dtos.event.EventResponseDto;
import com.gyp.eventservice.entities.EventEntity;
import com.gyp.eventservice.exceptions.EventNotFoundException;
import com.gyp.eventservice.mappers.EventMapper;
import com.gyp.eventservice.repositories.EventRepository;
import com.gyp.eventservice.services.criteria.EventSearchCriteria;
import com.gyp.eventservice.services.EventService;
import com.gyp.eventservice.services.specifications.EventSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
	private final EventRepository eventRepository;
	private final EventMapper eventMapper;

	@Override
	public List<EventEventModel> getListEventModel() {
		List<EventEntity> entities = eventRepository.findAll();
		return eventMapper.toModelList(entities);
	}

	@Override
	public List<EventResponseDto> getAllEvents() {
		List<EventEntity> entities = eventRepository.findAll();
		if(!entities.isEmpty()) {
			return eventMapper.toResponseDtoList(entities);
		}
		return null;
	}

	@Override
	public EventResponseDto getEventById(String id) throws EventNotFoundException {
		EventEntity entity = eventRepository.findById(id)
				.orElseThrow(() -> new EventNotFoundException("Event not found with id: " + id));
		if(entity != null) {
			return eventMapper.toResponseDto(entity);
		}
		return null;
	}

	@Override
	public EventResponseDto createEvent(EventRequestDto request) {
		return null;
	}

	@Override
	public EventResponseDto updateEvent(String eventId, EventRequestDto request) {
		return null;
	}

	@Override
	public EventResponseDto publishEvent(String eventId) {
		return null;
	}

	@Override
	public EventResponseDto cancelEvent(String eventId) {
		return null;
	}

	@Override
	public List<EventResponseDto> findEventsByOrganizer(String organizerId) {
		return List.of();
	}

	@Override
	public List<EventResponseDto> searchEvents(EventSearchCriteria criteria) {
		Specification<EventEntity> eventSpecification = EventSpecification.createSearchEventSpecification(criteria);
		List<EventEntity> entities = eventRepository.findAll(eventSpecification);
		return List.of();
	}

	@Override
	public EventResponseDto getEventDetails(String eventId) {
		return null;
	}
}
