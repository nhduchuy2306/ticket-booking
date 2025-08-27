package com.gyp.eventservice.controllers;

import java.util.Optional;

import com.gyp.common.controllers.AbstractController;
import com.gyp.common.dtos.pagination.PaginatedDto;
import com.gyp.common.services.DataIntegrityService;
import com.gyp.eventservice.dtos.venue.VenueRequestDto;
import com.gyp.eventservice.services.VenueService;
import com.gyp.eventservice.services.criteria.VenueSearchCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(VenueController.VENUE_CONTROLLER_PATH)
public class VenueController extends AbstractController {
	public static final String VENUE_CONTROLLER_PATH = "/venues";

	private final VenueService venueService;
	private final DataIntegrityService dataIntegrityService;

	@GetMapping
	@PreAuthorize("@permissionEvaluator.hasPermission(authentication, #AppPerm.VENUE, #ActionPerm.READ)")
	public ResponseEntity<?> getAllVenues(
			@RequestParam(value = "page", required = false) Optional<Integer> page,
			@RequestParam(value = "size", required = false) Optional<Integer> size) {
		try {
			String organizationId = getCurrentOrganizationId();
			VenueSearchCriteria criteria = VenueSearchCriteria.builder()
					.organizationId(organizationId)
					.build();
			PaginatedDto pagination = PaginatedDto.builder()
					.page(page.orElse(0))
					.size(size.orElse(10))
					.build();
			return ResponseEntity.ok(venueService.getAllVenues(criteria, pagination));
		} catch(RuntimeException e) {
			return ResponseEntity.status(404).body(e.getMessage());
		}
	}

	@GetMapping("/{" + ID_PARAM + "}")
	@PreAuthorize("@permissionEvaluator.hasPermission(authentication, #AppPerm.VENUE, #ActionPerm.READ)")
	public ResponseEntity<?> getVenueById(@PathVariable(ID_PARAM) String venueId) {
		try {
			return ResponseEntity.ok(venueService.getVenueById(venueId));
		} catch(RuntimeException e) {
			return ResponseEntity.status(404).body(e.getMessage());
		}
	}

	@PostMapping
	@PreAuthorize("@permissionEvaluator.hasPermission(authentication, #AppPerm.VENUE, #ActionPerm.CREATE)")
	public ResponseEntity<?> createVenue(@RequestBody VenueRequestDto venueDto) {
		try {
			return ResponseEntity.ok(venueService.createVenue(venueDto));
		} catch(RuntimeException e) {
			return ResponseEntity.status(400).body(e.getMessage());
		}
	}

	@PutMapping("/{" + ID_PARAM + "}")
	@PreAuthorize("@permissionEvaluator.hasPermission(authentication, #AppPerm.VENUE, #ActionPerm.UPDATE)")
	public ResponseEntity<?> updateVenue(@PathVariable(ID_PARAM) String venueId,
			@RequestBody VenueRequestDto venueDto) {
		try {
			return ResponseEntity.ok(venueService.updateVenue(venueId, venueDto));
		} catch(RuntimeException e) {
			return ResponseEntity.status(404).body(e.getMessage());
		}
	}

	@DeleteMapping("/{" + ID_PARAM + "}")
	@PreAuthorize("@permissionEvaluator.hasPermission(authentication, #AppPerm.VENUE, #ActionPerm.DELETE)")
	public ResponseEntity<?> deleteVenue(@PathVariable(ID_PARAM) String venueId) {
		try {
			venueService.deleteVenue(venueId);
			return ResponseEntity.noContent().build();
		} catch(RuntimeException e) {
			return ResponseEntity.status(404).body(e.getMessage());
		}
	}
}
