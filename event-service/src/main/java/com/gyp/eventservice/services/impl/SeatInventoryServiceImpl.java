package com.gyp.eventservice.services.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;

import com.gyp.common.converters.Serialization;
import com.gyp.common.enums.event.SeatHoldStatus;
import com.gyp.common.enums.event.SeatInventoryStatus;
import com.gyp.common.exceptions.ResourceNotFoundException;
import com.gyp.common.utils.HashUtils;
import com.gyp.eventservice.dtos.seatmap.Row;
import com.gyp.eventservice.dtos.seatmap.Seat;
import com.gyp.eventservice.dtos.seatmap.SeatAvailability;
import com.gyp.eventservice.dtos.seatmap.SeatConfig;
import com.gyp.eventservice.dtos.seatmap.SeatHoldRequestDto;
import com.gyp.eventservice.dtos.seatmap.SeatHoldResponseDto;
import com.gyp.eventservice.dtos.seatmap.Section;
import com.gyp.eventservice.dtos.seatmap.SectionType;
import com.gyp.eventservice.entities.EventEntity;
import com.gyp.eventservice.entities.SeatHoldEntity;
import com.gyp.eventservice.entities.SeatInventoryEntity;
import com.gyp.eventservice.entities.SeatMapEntity;
import com.gyp.eventservice.entities.VenueMapEntity;
import com.gyp.eventservice.repositories.EventRepository;
import com.gyp.eventservice.repositories.EventSectionMappingRepository;
import com.gyp.eventservice.repositories.SeatHoldRepository;
import com.gyp.eventservice.repositories.SeatInventoryRepository;
import com.gyp.eventservice.repositories.TicketTypeRepository;
import com.gyp.eventservice.services.EventCacheService;
import com.gyp.eventservice.services.SeatInventoryService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SeatInventoryServiceImpl implements SeatInventoryService {
	private static final int DEFAULT_HOLD_MINUTES = 15;

	private final EventRepository eventRepository;
	private final SeatInventoryRepository seatInventoryRepository;
	private final SeatHoldRepository seatHoldRepository;
	private final TicketTypeRepository ticketTypeRepository;
	private final EventSectionMappingRepository eventSectionMappingRepository;
	private final EventCacheService eventCacheService;

	@Override
	public List<SeatAvailability> getSeatAvailability(String eventId) {
		List<SeatAvailability> cachedSeatAvailability = eventCacheService.getSeatAvailability(eventId);
		if(cachedSeatAvailability != null) {
			return cachedSeatAvailability;
		}

		List<SeatInventoryEntity> seats = seatInventoryRepository.findByEventId(eventId);
		if(seats.isEmpty()) {
			return List.of();
		}

		Map<String, Double> priceByTicketTypeId = new java.util.HashMap<>();
		for(var ticketTypeEntity : ticketTypeRepository.findAllByEventEntityId(eventId)) {
			priceByTicketTypeId.put(ticketTypeEntity.getId(), ticketTypeEntity.getPrice());
		}
		List<SeatHoldEntity> activeHolds = seatHoldRepository.findByEventIdAndStatusIn(eventId,
				List.of(SeatHoldStatus.ACTIVE));
		Map<Object, SeatHoldEntity> holdBySeatId = activeHolds.stream()
				.collect(Collectors.toMap(hold -> hold.getSeatInventoryEntity().getId(), Function.identity(),
						(left, right) -> left));

		List<SeatAvailability> seatAvailabilityList = seats.stream()
				.map(seat -> toAvailability(seat, holdBySeatId.get(seat.getId()), priceByTicketTypeId))
				.toList();
		eventCacheService.cacheSeatAvailability(eventId, seatAvailabilityList);
		return seatAvailabilityList;
	}

	@Transactional
	@Override
	public SeatHoldResponseDto reserveSeats(SeatHoldRequestDto request) {
		String holdToken = normalizeHoldToken(request.getHoldToken());
		List<String> seatKeys = resolveSeatKeys(request);
		List<String> reserves = reserveSeatsForOrder(request.getEventId(), holdToken, request.getUserId(), seatKeys);
		return buildHoldResponse(request.getEventId(), holdToken, reserves);
	}

	@Transactional
	@Override
	public SeatHoldResponseDto confirmSeats(SeatHoldRequestDto request) {
		String holdToken = normalizeHoldToken(request.getHoldToken());
		List<String> seatKeys = confirmSeatsForOrder(request.getEventId(), holdToken);
		return buildHoldResponse(request.getEventId(), holdToken, seatKeys);
	}

	@Transactional
	@Override
	public SeatHoldResponseDto releaseSeats(SeatHoldRequestDto request) {
		String holdToken = normalizeHoldToken(request.getHoldToken());
		List<String> seatKeys = releaseSeatsForOrder(request.getEventId(), holdToken);
		return buildHoldResponse(request.getEventId(), holdToken, seatKeys);
	}

	@Transactional
	@Override
	public void initializeSeatsForEvent(String eventId) {
		if(seatInventoryRepository.existsByEventId(eventId)) {
			return;
		}

		EventEntity eventEntity = loadEvent(eventId);
		SeatConfig seatConfig = loadSeatConfig(eventEntity);
		List<SeatInventoryEntity> seats = new ArrayList<>();

		for(Section section : seatConfig.getSections()) {
			String ticketTypeId = eventCacheService.getTicketTypeId(eventId, section.getId());
			if(ticketTypeId == null) {
				var eventSectionMappingEntity = eventSectionMappingRepository.findByEventEntity_IdAndSectionId(
						eventId, section.getId());
				ticketTypeId = eventSectionMappingEntity.map(sectionMappingEntity ->
						sectionMappingEntity.getTicketTypeEntity().getId()).orElse(null);
				if(ticketTypeId != null) {
					eventCacheService.cacheTicketTypeId(eventId, section.getId(), ticketTypeId);
				}
			}
			if(Objects.equals(section.getType(), SectionType.SEATED)) {
				for(Row row : section.getRows()) {
					for(Seat seat : row.getSeats()) {
						seats.add(buildSeat(eventId, section, row.getId(), row.getName(), seat, ticketTypeId));
					}
				}
			}
		}

		seatInventoryRepository.saveAll(seats);
	}

	@Transactional
	@Override
	public List<String> reserveSeatsForOrder(String eventId, String holdToken, String userId, List<String> seatKeys) {
		if(CollectionUtils.isEmpty(seatKeys)) {
			return List.of();
		}

		List<String> requestedSeatKeys = new ArrayList<>(new LinkedHashSet<>(seatKeys));

		List<SeatHoldEntity> existingHolds = seatHoldRepository.findByEventIdAndHoldToken(eventId, holdToken);
		if(CollectionUtils.isNotEmpty(existingHolds)) {
			return existingHolds.stream()
					.map(hold -> hold.getSeatInventoryEntity().getSeatKey())
					.toList();
		}

		List<SeatInventoryEntity> seats = seatInventoryRepository.findByEventIdAndSeatKeyIn(eventId, requestedSeatKeys);
		validateSeatSet(eventId, requestedSeatKeys, seats);

		Map<String, SeatInventoryEntity> seatByKey = seats.stream()
				.collect(Collectors.toMap(SeatInventoryEntity::getSeatKey, Function.identity()));
		List<SeatHoldEntity> holds = new ArrayList<>();
		for(String seatKey : requestedSeatKeys) {
			SeatInventoryEntity seatInventoryEntity = seatByKey.get(seatKey);
			if(!SeatInventoryStatus.AVAILABLE.equals(seatInventoryEntity.getStatus())) {
				throw new IllegalStateException("Seat is not available: " + seatKey);
			}
			seatInventoryEntity.setStatus(SeatInventoryStatus.RESERVED);
			holds.add(SeatHoldEntity.builder()
					.eventId(eventId)
					.holdToken(holdToken)
					.userId(userId)
					.expiresAt(LocalDateTime.now().plusMinutes(DEFAULT_HOLD_MINUTES))
					.status(SeatHoldStatus.ACTIVE)
					.seatInventoryEntity(seatInventoryEntity)
					.build());
		}

		seatInventoryRepository.saveAll(seats);
		seatHoldRepository.saveAll(holds);
		eventCacheService.evictSeatAvailability(eventId);
		return seatKeys;
	}

	@Transactional
	@Override
	public List<String> confirmSeatsForOrder(String eventId, String holdToken) {
		List<SeatHoldEntity> activeHolds = seatHoldRepository.findByEventIdAndHoldTokenAndStatus(
				eventId, holdToken, SeatHoldStatus.ACTIVE);
		if(CollectionUtils.isEmpty(activeHolds)) {
			return List.of();
		}

		List<String> confirmedSeatKeys = new ArrayList<>();
		for(SeatHoldEntity seatHoldEntity : activeHolds) {
			SeatInventoryEntity seatInventoryEntity = seatHoldEntity.getSeatInventoryEntity();
			seatInventoryEntity.setStatus(SeatInventoryStatus.SOLD);
			seatHoldEntity.setStatus(SeatHoldStatus.CONFIRMED);
			confirmedSeatKeys.add(seatInventoryEntity.getSeatKey());
		}

		seatInventoryRepository.saveAll(activeHolds.stream().map(SeatHoldEntity::getSeatInventoryEntity).toList());
		seatHoldRepository.saveAll(activeHolds);
		eventCacheService.evictSeatAvailability(eventId);
		return confirmedSeatKeys;
	}

	@Transactional
	@Override
	public List<String> releaseSeatsForOrder(String eventId, String holdToken) {
		List<SeatHoldEntity> activeHolds = seatHoldRepository.findByEventIdAndHoldTokenAndStatus(
				eventId, holdToken, SeatHoldStatus.ACTIVE);
		if(CollectionUtils.isEmpty(activeHolds)) {
			return List.of();
		}

		List<String> releasedSeatKeys = new ArrayList<>();
		for(SeatHoldEntity seatHoldEntity : activeHolds) {
			SeatInventoryEntity seatInventoryEntity = seatHoldEntity.getSeatInventoryEntity();
			seatInventoryEntity.setStatus(SeatInventoryStatus.AVAILABLE);
			seatHoldEntity.setStatus(SeatHoldStatus.RELEASED);
			releasedSeatKeys.add(seatInventoryEntity.getSeatKey());
		}

		seatInventoryRepository.saveAll(activeHolds.stream().map(SeatHoldEntity::getSeatInventoryEntity).toList());
		seatHoldRepository.saveAll(activeHolds);
		eventCacheService.evictSeatAvailability(eventId);
		return releasedSeatKeys;
	}

	@Transactional
	@org.springframework.scheduling.annotation.Scheduled(fixedDelay = 60_000)
	public void releaseExpiredHolds() {
		List<SeatHoldEntity> expiredHolds = seatHoldRepository.findByStatusAndExpiresAtBefore(
				SeatHoldStatus.ACTIVE, LocalDateTime.now());
		if(CollectionUtils.isEmpty(expiredHolds)) {
			return;
		}

		List<SeatInventoryEntity> seatsToRelease = new ArrayList<>();
		for(SeatHoldEntity seatHoldEntity : expiredHolds) {
			SeatInventoryEntity seatInventoryEntity = seatHoldEntity.getSeatInventoryEntity();
			seatInventoryEntity.setStatus(SeatInventoryStatus.AVAILABLE);
			seatHoldEntity.setStatus(SeatHoldStatus.EXPIRED);
			seatsToRelease.add(seatInventoryEntity);
		}
		seatInventoryRepository.saveAll(seatsToRelease);
		seatHoldRepository.saveAll(expiredHolds);
		expiredHolds.stream()
				.map(SeatHoldEntity::getEventId)
				.filter(org.apache.commons.lang3.StringUtils::isNotBlank)
				.distinct()
				.forEach(eventCacheService::evictSeatAvailability);
	}

	private void validateSeatSet(String eventId, List<String> seatKeys, List<SeatInventoryEntity> seats) {
		Set<String> requested = new LinkedHashSet<>(seatKeys);
		Set<String> found = seats.stream().map(SeatInventoryEntity::getSeatKey).collect(Collectors.toSet());
		if(found.size() != requested.size()) {
			requested.removeAll(found);
			throw new ResourceNotFoundException("Seat not found for event " + eventId + ": " + requested);
		}
	}

	private SeatInventoryEntity buildSeat(String eventId, Section section, String containerId, String containerName,
			Seat seat, String ticketTypeId) {
		String seatKey = buildSeatKey(section, containerId, seat.getId());
		return SeatInventoryEntity.builder()
				.eventId(eventId)
				.seatKey(seatKey)
				.seatLabel(buildSeatLabel(section, containerName, seat))
				.sectionId(section.getId())
				.rowId(containerId)
				.ticketTypeId(ticketTypeId)
				.status(SeatInventoryStatus.AVAILABLE)
				.build();
	}

	private String buildSeatKey(Section section, String containerId, String seatId) {
		String key = section.getId() + "-" + containerId + "-" + seatId;
		return HashUtils.hashSha256(key);
	}

	private String buildSeatLabel(Section section, String containerName, Seat seat) {
		return section.getName() + "-" + containerName + "-" + seat.getName();
	}

	private EventEntity loadEvent(String eventId) {
		return eventRepository.findById(eventId)
				.orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + eventId));
	}

	private SeatConfig loadSeatConfig(EventEntity eventEntity) {
		VenueMapEntity venueMapEntity = eventEntity.getVenueMapEntity();
		if(venueMapEntity == null) {
			throw new ResourceNotFoundException("Venue map not found for event: " + eventEntity.getId());
		}
		SeatMapEntity seatMapEntity = venueMapEntity.getSeatMapEntity();
		if(seatMapEntity == null) {
			throw new ResourceNotFoundException("Seat map not found for event: " + eventEntity.getId());
		}
		return Serialization.deserializeFromString(seatMapEntity.getSeatConfigRaw(), SeatConfig.class);
	}

	private SeatAvailability toAvailability(SeatInventoryEntity seat, SeatHoldEntity hold,
			Map<String, Double> priceByTicketTypeId) {
		boolean isAvailable = SeatInventoryStatus.AVAILABLE.equals(seat.getStatus());
		return SeatAvailability.builder()
				.seatId(seat.getId())
				.seatKey(seat.getSeatKey())
				.seatLabel(seat.getSeatLabel())
				.sectionId(seat.getSectionId())
				.rowId(seat.getRowId())
				.ticketTypeId(seat.getTicketTypeId())
				.price(seat.getTicketTypeId() != null ? priceByTicketTypeId.get(seat.getTicketTypeId()) : null)
				.status(seat.getStatus())
				.available(isAvailable)
				.holdToken(hold != null ? hold.getHoldToken() : null)
				.holdExpiresAt(hold != null ? hold.getExpiresAt() : null)
				.build();
	}

	private SeatHoldResponseDto buildHoldResponse(String eventId, String holdToken, List<String> seatKeys) {
		List<SeatHoldEntity> holds = seatHoldRepository.findByEventIdAndHoldToken(eventId, holdToken);
		List<String> resolvedSeatIds = new ArrayList<>();
		if(!holds.isEmpty()) {
			resolvedSeatIds = holds.stream()
					.map(hold -> hold.getSeatInventoryEntity().getSeatKey())
					.toList();
		} else if(seatKeys != null) {
			resolvedSeatIds = new ArrayList<>(seatKeys);
		}

		LocalDateTime expiresAt = holds.stream()
				.filter(hold -> SeatHoldStatus.ACTIVE.equals(hold.getStatus()))
				.map(SeatHoldEntity::getExpiresAt)
				.findFirst()
				.orElse(null);

		return SeatHoldResponseDto.builder()
				.eventId(eventId)
				.holdToken(holdToken)
				.holdExpiresAt(expiresAt)
				.seatIds(resolvedSeatIds)
				.seats(getSeatAvailability(eventId))
				.build();
	}

	private List<String> resolveSeatKeys(SeatHoldRequestDto request) {
		if(request.getSeatKeys() != null && !request.getSeatKeys().isEmpty()) {
			return request.getSeatKeys();
		}
		if(request.getSeatIds() != null && !request.getSeatIds().isEmpty()) {
			return request.getSeatIds();
		}
		return List.of();
	}

	private String normalizeHoldToken(String holdToken) {
		return holdToken != null && !holdToken.isBlank() ? holdToken : java.util.UUID.randomUUID().toString();
	}
}