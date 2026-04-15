package com.gyp.eventservice.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.gyp.common.converters.Serialization;
import com.gyp.common.dtos.pagination.PaginatedDto;
import com.gyp.common.enums.event.EventStatus;
import com.gyp.common.exceptions.ResourceNotFoundException;
import com.gyp.common.services.UploadService;
import com.gyp.common.services.ValidationService;
import com.gyp.common.utils.SecurityUtils;
import com.gyp.common.validators.criteria.ValidationInfo;
import com.gyp.eventservice.dtos.event.EventRequestDto;
import com.gyp.eventservice.dtos.event.EventResponseDto;
import com.gyp.eventservice.dtos.seatmap.SeatConfig;
import com.gyp.eventservice.dtos.seatmap.Section;
import com.gyp.eventservice.entities.EventEntity;
import com.gyp.eventservice.entities.EventSectionMappingEntity;
import com.gyp.eventservice.entities.SeatMapEntity;
import com.gyp.eventservice.entities.TicketTypeEntity;
import com.gyp.eventservice.entities.VenueMapEntity;
import com.gyp.eventservice.mappers.EventMapper;
import com.gyp.eventservice.messages.producers.AssignSaleChannelToEventProducer;
import com.gyp.eventservice.repositories.EventRepository;
import com.gyp.eventservice.repositories.EventSectionMappingRepository;
import com.gyp.eventservice.repositories.VenueMapRepository;
import com.gyp.eventservice.services.EventService;
import com.gyp.eventservice.services.criteria.EventSearchCriteria;
import com.gyp.eventservice.services.specifications.EventSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
	private final EventRepository eventRepository;
	private final ValidationService validationService;
	private final EventMapper eventMapper;
	private final UploadService uploadService;
	private final AssignSaleChannelToEventProducer assignSaleChannelToEventProducer;
	private final VenueMapRepository venueMapRepository;
	private final EventSectionMappingRepository eventSectionMappingRepository;

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
			var event = eventMapper.toResponseDto(entity);
			setImageUrl(entity, event);
			return event;
		}
		return null;
	}

	@Override
	public EventResponseDto createEvent(EventRequestDto request) {
		String organizationId = SecurityUtils.getCurrentOrganizationId();
		EventEntity eventEntity = eventMapper.toEntity(request);
		enrichEventMappings(eventEntity);
		eventEntity.setOrganizationId(organizationId);
		var savedEvent = eventRepository.save(eventEntity);
		updateSaleChannels(savedEvent.getId(), request.getSaleChannelIds());
		return eventMapper.toResponseDto(savedEvent);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public EventResponseDto createEvent(EventRequestDto request, MultipartFile file) {
		String organizationId = SecurityUtils.getCurrentOrganizationId();
		EventEntity eventEntity = eventMapper.toEntity(request);
		eventEntity.setIsGenerated(false);
		enrichEventMappings(eventEntity);
		if(file != null) {
			String fileName = uploadService.upload(file).getLeft();
			eventEntity.setLogoUrl(fileName);
		}
		eventEntity.setOrganizationId(organizationId);
		EventEntity savedEvent = eventRepository.save(eventEntity);
		updateSaleChannels(savedEvent.getId(), request.getSaleChannelIds());
		return eventMapper.toResponseDto(savedEvent);
	}

	@Override
	public EventResponseDto updateEvent(String eventId, EventRequestDto request) {
		String organizationId = SecurityUtils.getCurrentOrganizationId();
		EventEntity existingEvent = eventRepository.findById(eventId)
				.orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + eventId));
		eventMapper.updateEntityFromDto(request, existingEvent);
		enrichEventMappings(existingEvent);
		existingEvent.setOrganizationId(organizationId);
		EventEntity updatedEvent = eventRepository.save(existingEvent);
		updateSaleChannels(eventId, request.getSaleChannelIds());
		return eventMapper.toResponseDto(updatedEvent);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public EventResponseDto updateEvent(String eventId, EventRequestDto request, MultipartFile file)
			throws ResourceNotFoundException {
		try {
			String organizationId = SecurityUtils.getCurrentOrganizationId();
			EventEntity existingEvent = eventRepository.findById(eventId)
					.orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + eventId));
			eventMapper.updateEntityFromDto(request, existingEvent);
			enrichEventMappings(existingEvent);
			if(file != null) {
				// Delete old logo if it exists
				if(existingEvent.getLogoUrl() != null) {
					uploadService.deleteFile(existingEvent.getLogoUrl());
				}
				String fileName = uploadService.upload(file).getLeft();
				existingEvent.setLogoUrl(fileName);
			}
			existingEvent.setOrganizationId(organizationId);
			EventEntity updatedEvent = eventRepository.save(existingEvent);
			updateSaleChannels(eventId, request.getSaleChannelIds());
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
	public List<EventResponseDto> getAllEventsOnSale() {
		return eventRepository.findAllEventsOnSale()
				.stream()
				.map(item -> {
					var event = eventMapper.toResponseDto(item);
					setImageUrl(item, event);
					return event;
				})
				.toList();
	}

	@Override
	public List<EventResponseDto> getAllComingEvents() {
		return eventRepository.findAllEventsComing()
				.stream()
				.map(item -> {
					var event = eventMapper.toResponseDto(item);
					setImageUrl(item, event);
					return event;
				})
				.toList();
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

	private void updateSaleChannels(String eventId, List<String> saleChannelIds) {
		if(CollectionUtils.isNotEmpty(saleChannelIds)) {
			assignSaleChannelToEventProducer.assignSaleChannelToEventProducer(eventId, saleChannelIds);
		} else {
			assignSaleChannelToEventProducer.assignSaleChannelToEventProducer(eventId, new ArrayList<>());
		}
	}

	private void setImageUrl(EventEntity entity, EventResponseDto event) {
		byte[] logoBufferArray = StringUtils.isNotEmpty(entity.getLogoUrl())
				? uploadService.getFileData(entity.getLogoUrl())
				: null;
		event.setLogoBufferArray(logoBufferArray);
	}

	private void enrichEventMappings(EventEntity eventEntity) {
		if(eventEntity == null || eventEntity.getVenueMapEntity() == null ||
				CollectionUtils.isEmpty(eventEntity.getEventSectionMappingEntityList())) {
			return;
		}

		VenueMapEntity venueMapEntity = venueMapRepository.findById(eventEntity.getVenueMapEntity().getId())
				.orElse(null);
		if(venueMapEntity == null) {
			return;
		}

		eventEntity.setVenueMapEntity(venueMapEntity);
		SeatMapEntity seatMapEntity = venueMapEntity.getSeatMapEntity();
		Map<String, String> sectionIdByTicketTypeId = resolveSectionIds(seatMapEntity);
		Map<String, EventSectionMappingEntity> existingMappingByKey = loadExistingMappingsByKey(eventEntity.getId());
		Map<String, EventSectionMappingEntity> reconciledMappings = new LinkedHashMap<>();

		for(EventSectionMappingEntity mapping : eventEntity.getEventSectionMappingEntityList()) {
			String ticketTypeId = mapping.getTicketTypeEntity() != null ? mapping.getTicketTypeEntity().getId() : null;
			String sectionId = ticketTypeId != null ? sectionIdByTicketTypeId.get(ticketTypeId) : null;
			String key = buildMappingKey(eventEntity.getId(), ticketTypeId,
					seatMapEntity != null ? seatMapEntity.getId() : null,
					sectionId);
			if(StringUtils.isBlank(ticketTypeId) || StringUtils.isBlank(sectionId) || StringUtils.isBlank(key)) {
				continue;
			}

			EventSectionMappingEntity target = existingMappingByKey.getOrDefault(key, mapping);
			target.setEventEntity(eventEntity);
			target.setSeatMapEntity(seatMapEntity);
			target.setSectionId(sectionId);
			if(target.getTicketTypeEntity() == null || !ticketTypeId.equals(target.getTicketTypeEntity().getId())) {
				target.setTicketTypeEntity(TicketTypeEntity.builder().id(ticketTypeId).build());
			}
			reconciledMappings.put(key, target);
		}

		eventEntity.setEventSectionMappingEntityList(new ArrayList<>(reconciledMappings.values()));
	}

	private Map<String, EventSectionMappingEntity> loadExistingMappingsByKey(String eventId) {
		if(StringUtils.isBlank(eventId)) {
			return new HashMap<>();
		}

		List<EventSectionMappingEntity> existingMappings = eventSectionMappingRepository.findAllByEventEntityId(
				eventId);
		if(CollectionUtils.isEmpty(existingMappings)) {
			return new HashMap<>();
		}

		Map<String, EventSectionMappingEntity> mappingsByKey = new HashMap<>();
		for(EventSectionMappingEntity mapping : existingMappings) {
			String ticketTypeId = mapping.getTicketTypeEntity() != null ? mapping.getTicketTypeEntity().getId() : null;
			String seatMapId = mapping.getSeatMapEntity() != null ? mapping.getSeatMapEntity().getId() : null;
			String key = buildMappingKey(eventId, ticketTypeId, seatMapId, mapping.getSectionId());
			if(StringUtils.isNotBlank(key)) {
				mappingsByKey.put(key, mapping);
			}
		}
		return mappingsByKey;
	}

	private String buildMappingKey(String eventId, String ticketTypeId, String seatMapId, String sectionId) {
		if(StringUtils.isAnyBlank(eventId, ticketTypeId, seatMapId, sectionId)) {
			return null;
		}
		return eventId + "|" + ticketTypeId + "|" + seatMapId + "|" + sectionId;
	}

	private Map<String, String> resolveSectionIds(SeatMapEntity seatMapEntity) {
		if(seatMapEntity == null || StringUtils.isBlank(seatMapEntity.getSeatConfigRaw())) {
			return new HashMap<>();
		}

		try {
			SeatConfig seatConfig = Serialization.deserializeFromString(seatMapEntity.getSeatConfigRaw(),
					SeatConfig.class);
			if(seatConfig == null || CollectionUtils.isEmpty(seatConfig.getSections())) {
				return new HashMap<>();
			}

			return seatConfig.getSections().stream()
					.filter(section -> StringUtils.isNotBlank(section.getTicketTypeId())
							&& StringUtils.isNotBlank(section.getId()))
					.collect(Collectors.toMap(Section::getTicketTypeId, Section::getId, (left, right) -> left,
							HashMap::new));
		} catch(Exception ex) {
			log.warn("Failed to resolve section ids from seat map {}", seatMapEntity.getId(), ex);
			return new HashMap<>();
		}
	}
}
