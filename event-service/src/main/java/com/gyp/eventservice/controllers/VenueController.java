package com.gyp.eventservice.controllers;

import com.gyp.common.controllers.AbstractController;
import com.gyp.eventservice.dtos.venue.VenueRequestDto;
import com.gyp.eventservice.exceptions.VenueNotFoundException;
import com.gyp.eventservice.services.VenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(VenueController.VENUE_CONTROLLER_PATH)
public class VenueController extends AbstractController {
	public static final String VENUE_CONTROLLER_PATH = "/venues";

	private final VenueService venueService;

	@GetMapping
	public ResponseEntity<?> getVenues() {
		try {
			return ResponseEntity.ok(venueService.getVenues());
		} catch(RuntimeException e) {
			return ResponseEntity.status(404).body(e.getMessage());
		}
	}

	@GetMapping("/{" + ID_PARAM + "}")
	public ResponseEntity<?> getVenueById(@PathVariable(ID_PARAM) String venueId) {
		try {
			return ResponseEntity.ok(venueService.getVenueById(venueId));
		} catch(RuntimeException e) {
			return ResponseEntity.status(404).body(e.getMessage());
		} catch(VenueNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	@PostMapping
	public ResponseEntity<?> createVenue(@RequestBody VenueRequestDto venueDto) {
		try {
			return ResponseEntity.ok(venueService.createVenue(venueDto));
		} catch(RuntimeException e) {
			return ResponseEntity.status(400).body(e.getMessage());
		}
	}

	@PutMapping("/{" + ID_PARAM + "}")
	public ResponseEntity<?> updateVenue(@PathVariable(ID_PARAM) String venueId,
			@RequestBody VenueRequestDto venueDto) {
		try {
			return ResponseEntity.ok(venueService.updateVenue(venueId, venueDto));
		} catch(RuntimeException e) {
			return ResponseEntity.status(404).body(e.getMessage());
		}
	}

	@DeleteMapping("/{" + ID_PARAM + "}")
	public ResponseEntity<?> deleteVenue(@PathVariable(ID_PARAM) String venueId) {
		try {
			venueService.deleteVenue(venueId);
			return ResponseEntity.noContent().build();
		} catch(RuntimeException e) {
			return ResponseEntity.status(404).body(e.getMessage());
		}
	}
}
