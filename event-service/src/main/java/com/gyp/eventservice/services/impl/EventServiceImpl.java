package com.gyp.eventservice.services.impl;

import java.util.List;

import com.gyp.common.dtos.pagination.PaginatedDto;
import com.gyp.common.enums.event.EventStatus;
import com.gyp.common.exceptions.ResourceNotFoundException;
import com.gyp.common.models.EventEventModel;
import com.gyp.common.services.UploadService;
import com.gyp.common.services.ValidationService;
import com.gyp.common.utils.SecurityUtils;
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
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
	private final EventRepository eventRepository;
	private final ValidationService validationService;
	private final EventMapper eventMapper;
	private final CheckFactory checkFactory;
	private final UploadService uploadService;

	@Override
	public List<EventEventModel> getListEventModel() {
		List<EventEntity> entities = eventRepository.findAll();
		return eventMapper.toModelList(entities);
	}

	@Override
	public List<EventResponseDto> getAllEvents(EventSearchCriteria criteria, PaginatedDto pagination) {
		Specification<EventEntity> eventSpecification = EventSpecification.createSearchEventSpecification(criteria);
		Page<EventEntity> entities = eventRepository.findAll(eventSpecification, pagination.toPageable());
		if(!entities.isEmpty()) {
			return eventMapper.toResponseDtoList(entities.getContent());
		}
		return null;
	}

	@Override
	public EventResponseDto getEventById(String id) throws ResourceNotFoundException {
		EventEntity entity = eventRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));
		if(entity != null) {
			String logoUrl = uploadService.getFileUrl(entity.getLogoUrl());
			var event = eventMapper.toResponseDto(entity);
			event.setLogoUrl(logoUrl);
			return event;
		}
		return null;
	}

	@Override
	public EventResponseDto createEvent(EventRequestDto request) {
		String organizationId = SecurityUtils.getCurrentOrganizationId();
		EventEntity eventEntity = eventMapper.toEntity(request);
		eventEntity.setOrganizationId(organizationId);
		var saveEvent = eventRepository.save(eventEntity);
		return eventMapper.toResponseDto(saveEvent);
	}

	@Override
	public EventResponseDto createEvent(EventRequestDto request, MultipartFile file) {
		String organizationId = SecurityUtils.getCurrentOrganizationId();
		EventEntity eventEntity = eventMapper.toEntity(request);
		if(file != null) {
			String fileName = uploadService.upload(file);
			eventEntity.setLogoUrl(fileName);
		}
		eventEntity.setOrganizationId(organizationId);
		EventEntity savedEvent = eventRepository.save(eventEntity);
		return eventMapper.toResponseDto(savedEvent);
	}

	@Override
	public EventResponseDto updateEvent(String eventId, EventRequestDto request) {
		String organizationId = SecurityUtils.getCurrentOrganizationId();
		EventEntity existingEvent = eventRepository.findById(eventId)
				.orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + eventId));
		eventMapper.updateEntityFromDto(request, existingEvent);
		existingEvent.setOrganizationId(organizationId);
		EventEntity updatedEvent = eventRepository.save(existingEvent);
		return eventMapper.toResponseDto(updatedEvent);
	}

	@Override
	public EventResponseDto updateEvent(String eventId, EventRequestDto request, MultipartFile file)
			throws ResourceNotFoundException {
		try {
			String organizationId = SecurityUtils.getCurrentOrganizationId();
			EventEntity existingEvent = eventRepository.findById(eventId)
					.orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + eventId));
			eventMapper.updateEntityFromDto(request, existingEvent);
			if(file != null) {
				// Delete old logo if it exists
				if(existingEvent.getLogoUrl() != null) {
					uploadService.deleteFile(existingEvent.getLogoUrl());
				}
				String fileName = uploadService.upload(file);
				existingEvent.setLogoUrl(fileName);
			}
			existingEvent.setOrganizationId(organizationId);
			EventEntity updatedEvent = eventRepository.save(existingEvent);
			return eventMapper.toResponseDto(updatedEvent);
		} catch(Exception e) {
			log.error("Error updating event with id: {}", eventId, e);
			throw new ResourceNotFoundException("Failed to update event with id: " + eventId, e);
		}
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
	public List<EventResponseDto> getAllActiveEvents() {
		String organizationId = SecurityUtils.getCurrentOrganizationId();
		var events = eventRepository.findAllActiveEvents(organizationId);
		if(events != null && !events.isEmpty()) {
			return eventMapper.toResponseDtoList(events);
		}
		return null;
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
