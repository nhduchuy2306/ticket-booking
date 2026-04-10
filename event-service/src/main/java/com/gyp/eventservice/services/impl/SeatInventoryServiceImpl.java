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
import com.gyp.common.exceptions.ResourceNotFoundException;
import com.gyp.eventservice.dtos.seatmap.SeatAvailability;
import com.gyp.eventservice.dtos.seatmap.SeatHoldRequestDto;
import com.gyp.eventservice.dtos.seatmap.SeatHoldResponseDto;
import com.gyp.eventservice.dtos.seatmap.Row;
import com.gyp.eventservice.dtos.seatmap.Seat;
import com.gyp.eventservice.dtos.seatmap.SeatConfig;
import com.gyp.eventservice.dtos.seatmap.Section;
import com.gyp.eventservice.dtos.seatmap.SeatStatus;
import com.gyp.eventservice.dtos.seatmap.SectionType;
import com.gyp.eventservice.dtos.seatmap.Table;
import com.gyp.eventservice.entities.EventEntity;
import com.gyp.eventservice.entities.SeatEntity;
import com.gyp.eventservice.entities.SeatHoldEntity;
import com.gyp.eventservice.entities.SeatHoldStatus;
import com.gyp.eventservice.entities.SeatInventoryStatus;
import com.gyp.eventservice.entities.SeatMapEntity;
import com.gyp.eventservice.entities.VenueMapEntity;
import com.gyp.eventservice.repositories.EventRepository;
import com.gyp.eventservice.repositories.SeatHoldRepository;
import com.gyp.eventservice.repositories.SeatRepository;
import com.gyp.eventservice.repositories.TicketTypeRepository;
import com.gyp.eventservice.services.SeatInventoryService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SeatInventoryServiceImpl implements SeatInventoryService {
	private static final int DEFAULT_HOLD_MINUTES = 15;

	private final EventRepository eventRepository;
	private final SeatRepository seatRepository;
	private final SeatHoldRepository seatHoldRepository;
	private final TicketTypeRepository ticketTypeRepository;

	@Transactional
	@Override
	public List<SeatAvailability> getSeatAvailability(String eventId) {
		List<SeatEntity> seats = seatRepository.findByEventId(eventId);
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
				.collect(Collectors.toMap(hold -> hold.getSeatEntity().getId(), Function.identity(),
						(left, right) -> left));

		return seats.stream()
				.map(seat -> toAvailability(seat, holdBySeatId.get(seat.getId()), priceByTicketTypeId))
				.toList();
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
		if(seatRepository.existsByEventId(eventId)) {
			return;
		}

		EventEntity eventEntity = loadEvent(eventId);
		SeatConfig seatConfig = loadSeatConfig(eventEntity);
		List<SeatEntity> seats = new ArrayList<>();

		for(Section section : seatConfig.getSections()) {
			if(Objects.equals(section.getType(), SectionType.SEATED)) {
				for(Row row : section.getRows()) {
					for(Seat seat : row.getSeats()) {
						seats.add(buildSeat(eventId, section, row.getId(), row.getName(), seat));
					}
				}
			} else if(Objects.equals(section.getType(), SectionType.TABLE)) {
				for(Table table : section.getTables()) {
					for(Seat seat : table.getSeats()) {
						seats.add(buildSeat(eventId, section, table.getId(), table.getName(), seat));
					}
				}
			}
		}

		seatRepository.saveAll(seats);
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
					.map(hold -> hold.getSeatEntity().getSeatKey())
					.toList();
		}

		List<SeatEntity> seats = seatRepository.findByEventIdAndSeatKeyIn(eventId, requestedSeatKeys);
		validateSeatSet(eventId, requestedSeatKeys, seats);

		Map<String, SeatEntity> seatByKey = seats.stream()
				.collect(Collectors.toMap(SeatEntity::getSeatKey, Function.identity()));
		List<SeatHoldEntity> holds = new ArrayList<>();
		for(String seatKey : requestedSeatKeys) {
			SeatEntity seatEntity = seatByKey.get(seatKey);
			if(!SeatInventoryStatus.AVAILABLE.equals(seatEntity.getStatus())) {
				throw new IllegalStateException("Seat is not available: " + seatKey);
			}
			seatEntity.setStatus(SeatInventoryStatus.RESERVED);
			holds.add(SeatHoldEntity.builder()
					.eventId(eventId)
					.holdToken(holdToken)
					.userId(userId)
					.expiresAt(LocalDateTime.now().plusMinutes(DEFAULT_HOLD_MINUTES))
					.status(SeatHoldStatus.ACTIVE)
					.seatEntity(seatEntity)
					.build());
		}

		seatRepository.saveAll(seats);
		seatHoldRepository.saveAll(holds);
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
			SeatEntity seatEntity = seatHoldEntity.getSeatEntity();
			seatEntity.setStatus(SeatInventoryStatus.SOLD);
			seatHoldEntity.setStatus(SeatHoldStatus.CONFIRMED);
			confirmedSeatKeys.add(seatEntity.getSeatKey());
		}

		seatRepository.saveAll(activeHolds.stream().map(SeatHoldEntity::getSeatEntity).toList());
		seatHoldRepository.saveAll(activeHolds);
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
			SeatEntity seatEntity = seatHoldEntity.getSeatEntity();
			seatEntity.setStatus(SeatInventoryStatus.AVAILABLE);
			seatHoldEntity.setStatus(SeatHoldStatus.RELEASED);
			releasedSeatKeys.add(seatEntity.getSeatKey());
		}

		seatRepository.saveAll(activeHolds.stream().map(SeatHoldEntity::getSeatEntity).toList());
		seatHoldRepository.saveAll(activeHolds);
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

		List<SeatEntity> seatsToRelease = new ArrayList<>();
		for(SeatHoldEntity seatHoldEntity : expiredHolds) {
			SeatEntity seatEntity = seatHoldEntity.getSeatEntity();
			seatEntity.setStatus(SeatInventoryStatus.AVAILABLE);
			seatHoldEntity.setStatus(SeatHoldStatus.EXPIRED);
			seatsToRelease.add(seatEntity);
		}
		seatRepository.saveAll(seatsToRelease);
		seatHoldRepository.saveAll(expiredHolds);
	}

	private void validateSeatSet(String eventId, List<String> seatKeys, List<SeatEntity> seats) {
		Set<String> requested = new LinkedHashSet<>(seatKeys);
		Set<String> found = seats.stream().map(SeatEntity::getSeatKey).collect(Collectors.toSet());
		if(found.size() != requested.size()) {
			requested.removeAll(found);
			throw new ResourceNotFoundException("Seat not found for event " + eventId + ": " + requested);
		}
	}

	private SeatEntity buildSeat(String eventId, Section section, String containerId, String containerName, Seat seat) {
		String seatKey = buildSeatKey(section, containerId, seat.getId());
		return SeatEntity.builder()
				.eventId(eventId)
				.seatKey(seatKey)
				.seatLabel(buildSeatLabel(section, containerName, seat))
				.sectionId(section.getId())
				.rowId(containerId)
				.ticketTypeId(seat.getTicketTypeId())
				.status(toInventoryStatus(seat.getStatus()))
				.build();
	}

	private SeatInventoryStatus toInventoryStatus(SeatStatus seatStatus) {
		if(seatStatus == null) {
			return SeatInventoryStatus.AVAILABLE;
		}
		return switch(seatStatus) {
			case AVAILABLE -> SeatInventoryStatus.AVAILABLE;
			case RESERVED -> SeatInventoryStatus.RESERVED;
			case SOLD -> SeatInventoryStatus.SOLD;
		};
	}

	private String buildSeatKey(Section section, String containerId, String seatId) {
		return section.getId() + "-" + containerId + "-" + seatId;
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
		try {
			return Serialization.deserializeFromString(seatMapEntity.getSeatConfigRaw(), SeatConfig.class);
		} catch(Exception e) {
			throw new IllegalStateException("Failed to parse seat map for event: " + eventEntity.getId(), e);
		}
	}

	private SeatAvailability toAvailability(SeatEntity seat, SeatHoldEntity hold, Map<String, Double> priceByTicketTypeId) {
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
					.map(hold -> hold.getSeatEntity().getSeatKey())
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