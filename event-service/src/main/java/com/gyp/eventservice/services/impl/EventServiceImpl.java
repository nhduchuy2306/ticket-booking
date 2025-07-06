package com.gyp.eventservice.services.impl;

import java.util.List;
import java.util.Map;

import com.gyp.common.enums.event.EventStatus;
import com.gyp.common.exceptions.ResourceDuplicateException;
import com.gyp.common.exceptions.ResourceNotFoundException;
import com.gyp.common.models.EventEventModel;
import com.gyp.common.services.ValidationService;
import com.gyp.common.utils.PropertyName;
import com.gyp.common.validators.criteria.ValidationInfo;
import com.gyp.common.validators.rulecheck.CheckFactory;
import com.gyp.eventservice.dtos.event.EventRequestDto;
import com.gyp.eventservice.dtos.event.EventResponseDto;
import com.gyp.eventservice.entities.EventEntity;
import com.gyp.eventservice.mappers.EventMapper;
import com.gyp.eventservice.repositories.EventRepository;
import com.gyp.eventservice.services.EventService;
import com.gyp.eventservice.services.criteria.EventSearchCriteria;
import com.gyp.eventservice.services.specifications.EventSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
	private final EventRepository eventRepository;
	private final ValidationService validationService;
	private final EventMapper eventMapper;
	private final CheckFactory checkFactory;

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
	public EventResponseDto getEventById(String id) throws ResourceNotFoundException {
		EventEntity entity = eventRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));
		if(entity != null) {
			return eventMapper.toResponseDto(entity);
		}
		return null;
	}

	@Override
	public EventResponseDto createEvent(EventRequestDto request) {
		var checkDuplicate = checkFactory.createMultiFieldDuplicateCheck(EventEntity.class,
				Map.of(
						PropertyName.of(request::getOrganizerId), request.getOrganizerId(),
						PropertyName.of(request::getName), request.getName(),
						PropertyName.of(request::getSeasonId), request.getSeasonId()
				));
		if(!checkDuplicate.isValid()) {
			throw new ResourceDuplicateException(
					"Duplicate event found: " + "Organizer ID: " + request.getOrganizerId() +
							", Name: " + request.getName() + ", Season ID: " + request.getSeasonId());
		}
		EventEntity eventEntity = eventMapper.toEntity(request);
		var saveEvent = eventRepository.save(eventEntity);
		return eventMapper.toResponseDto(saveEvent);
	}

	@Override
	public EventResponseDto updateEvent(String eventId, EventRequestDto request) {
		EventEntity existingEvent = eventRepository.findById(eventId)
				.orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + eventId));
		var checkDuplicate = checkFactory.createMultiFieldDuplicateCheck(EventEntity.class,
				Map.of(
						PropertyName.of(request::getOrganizerId), request.getOrganizerId(),
						PropertyName.of(request::getName), request.getName(),
						PropertyName.of(request::getSeasonId), request.getSeasonId()
				));
		if(!checkDuplicate.isValid()) {
			throw new ResourceDuplicateException(
					"Duplicate event found: " + "Organizer ID: " + request.getOrganizerId() +
							", Name: " + request.getName() + ", Season ID: " + request.getSeasonId());
		}
		EventEntity eventEntity = eventMapper.toEntity(request);
		eventMapper.updateEntityFromDto(request, existingEvent);

		EventEntity updatedEvent = eventRepository.save(existingEvent);
		return eventMapper.toResponseDto(updatedEvent);
	}

	@Override
	public EventResponseDto publishEvent(String eventId) {
		EventEntity eventEntity = eventRepository.findById(eventId)
				.orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + eventId));
		eventEntity.setStatus(EventStatus.PUBLISHED);
		EventEntity updatedEvent = eventRepository.save(eventEntity);
		return eventMapper.toResponseDto(updatedEvent);
	}

	@Override
	public EventResponseDto cancelEvent(String eventId) {
		EventEntity eventEntity = eventRepository.findById(eventId)
				.orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + eventId));
		eventEntity.setStatus(EventStatus.CANCELLED);
		EventEntity updatedEvent = eventRepository.save(eventEntity);
		return eventMapper.toResponseDto(updatedEvent);
	}

	@Override
	public List<EventResponseDto> searchEvents(EventSearchCriteria criteria) {
		Specification<EventEntity> eventSpecification = EventSpecification.createSearchEventSpecification(criteria);
		List<EventEntity> entities = eventRepository.findAll(eventSpecification);
		return eventMapper.toResponseDtoList(entities);
	}

	@Override
	public EventResponseDto getEventDetails(String eventId) {
		return null;
	}

	@Override
	public void deleteEvent(String id) throws ResourceNotFoundException {
		EventEntity eventEntity = eventRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));
		eventRepository.delete(eventEntity);
	}

	@Override
	public ValidationInfo validate(Class<?> clazz) {
		var validationInfo = validationService.extractValidationInfo(clazz);
		if(validationInfo == null) {
			log.warn("No validation info found for request class: {}", clazz.getName());
			return ValidationInfo.empty();
		}
		return validationInfo;
	}
}
