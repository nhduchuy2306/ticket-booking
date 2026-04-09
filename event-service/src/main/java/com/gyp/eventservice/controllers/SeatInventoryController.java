package com.gyp.eventservice.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.gyp.eventservice.dtos.seatmap.SeatAvailability;
import com.gyp.eventservice.dtos.seatmap.SeatHoldRequestDto;
import com.gyp.eventservice.dtos.seatmap.SeatHoldResponseDto;
import com.gyp.eventservice.entities.SeatEntity;
import com.gyp.eventservice.entities.SeatHoldEntity;
import com.gyp.eventservice.entities.SeatHoldStatus;
import com.gyp.eventservice.entities.SeatInventoryStatus;
import com.gyp.eventservice.entities.TicketTypeEntity;
import com.gyp.eventservice.repositories.SeatHoldRepository;
import com.gyp.eventservice.repositories.SeatRepository;
import com.gyp.eventservice.repositories.TicketTypeRepository;
import com.gyp.eventservice.services.SeatInventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(SeatInventoryController.SEAT_INVENTORY_CONTROLLER_RESOURCE_PATH)
public class SeatInventoryController {
	public static final String SEAT_INVENTORY_CONTROLLER_RESOURCE_PATH = "/seat-inventory";

	private final SeatInventoryService seatInventoryService;
	private final SeatRepository seatRepository;
	private final SeatHoldRepository seatHoldRepository;
	private final TicketTypeRepository ticketTypeRepository;

	@GetMapping("/availability")
	public ResponseEntity<List<SeatAvailability>> getSeatAvailability(@RequestParam String eventId) {
		return ResponseEntity.ok(buildAvailability(eventId));
	}

	@PostMapping("/reserve")
	public ResponseEntity<SeatHoldResponseDto> reserveSeats(@RequestBody SeatHoldRequestDto request) {
		String holdToken = normalizeHoldToken(request.getHoldToken());
		List<String> seatKeys = resolveSeatKeys(request);
		List<String> reserves = seatInventoryService.reserveSeatsForOrder(request.getEventId(), holdToken,
				request.getUserId(), seatKeys);
		log.info("Reserved seats for {}", reserves);
		return ResponseEntity.ok(buildHoldResponse(request.getEventId(), holdToken, seatKeys));
	}

	@PostMapping("/confirm")
	public ResponseEntity<SeatHoldResponseDto> confirmSeats(@RequestBody SeatHoldRequestDto request) {
		String holdToken = normalizeHoldToken(request.getHoldToken());
		List<String> seatKeys = seatInventoryService.confirmSeatsForOrder(request.getEventId(), holdToken);
		return ResponseEntity.ok(buildHoldResponse(request.getEventId(), holdToken, seatKeys));
	}

	@PostMapping("/release")
	public ResponseEntity<SeatHoldResponseDto> releaseSeats(@RequestBody SeatHoldRequestDto request) {
		String holdToken = normalizeHoldToken(request.getHoldToken());
		List<String> seatKeys = seatInventoryService.releaseSeatsForOrder(request.getEventId(), holdToken);
		return ResponseEntity.ok(buildHoldResponse(request.getEventId(), holdToken, seatKeys));
	}

	private List<SeatAvailability> buildAvailability(String eventId) {
		List<SeatEntity> seats = seatRepository.findByEventId(eventId);
		if(seats.isEmpty()) {
			return List.of();
		}

		Map<String, Double> priceByTicketTypeId = new java.util.HashMap<>();
		for(TicketTypeEntity ticketTypeEntity : ticketTypeRepository.findAllByEventEntityId(eventId)) {
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

	private SeatAvailability toAvailability(SeatEntity seat, SeatHoldEntity hold,
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

	private SeatHoldResponseDto buildHoldResponse(String eventId, String holdToken, Collection<String> seatKeys) {
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
				.seats(buildAvailability(eventId))
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
		return holdToken != null && !holdToken.isBlank() ? holdToken : UUID.randomUUID().toString();
	}
}
