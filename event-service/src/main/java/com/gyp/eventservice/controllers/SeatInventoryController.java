package com.gyp.eventservice.controllers;

import java.util.List;

import com.gyp.eventservice.dtos.seatmap.SeatAvailability;
import com.gyp.eventservice.dtos.seatmap.SeatHoldRequestDto;
import com.gyp.eventservice.dtos.seatmap.SeatHoldResponseDto;
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

	@GetMapping("/availability")
	public ResponseEntity<List<SeatAvailability>> getSeatAvailability(@RequestParam String eventId) {
		return ResponseEntity.ok(seatInventoryService.getSeatAvailability(eventId));
	}

	@PostMapping("/reserve")
	public ResponseEntity<SeatHoldResponseDto> reserveSeats(@RequestBody SeatHoldRequestDto request) {
		SeatHoldResponseDto response = seatInventoryService.reserveSeats(request);
		log.info("Reserved seats for {}", response.getSeatIds());
		return ResponseEntity.ok(response);
	}

	@PostMapping("/confirm")
	public ResponseEntity<SeatHoldResponseDto> confirmSeats(@RequestBody SeatHoldRequestDto request) {
		return ResponseEntity.ok(seatInventoryService.confirmSeats(request));
	}

	@PostMapping("/release")
	public ResponseEntity<SeatHoldResponseDto> releaseSeats(@RequestBody SeatHoldRequestDto request) {
		return ResponseEntity.ok(seatInventoryService.releaseSeats(request));
	}
}
