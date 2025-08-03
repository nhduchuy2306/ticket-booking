package com.gyp.eventservice.controllers;

import java.util.Optional;

import com.gyp.common.controllers.AbstractController;
import com.gyp.common.dtos.pagination.PaginatedDto;
import com.gyp.eventservice.dtos.venuemap.VenueMapRequestDto;
import com.gyp.eventservice.services.VenueMapService;
import com.gyp.eventservice.services.criteria.VenueMapSearchCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
@RequestMapping(VenueMapController.VENUE_MAP_CONTROLLER_RESOURCE_PATH)
public class VenueMapController extends AbstractController {
	public static final String VENUE_MAP_CONTROLLER_RESOURCE_PATH = "/venue-maps";

	private final VenueMapService venueMapService;

	@GetMapping
	public ResponseEntity<?> getAllVenueMaps(
			@RequestParam(value = "page", required = false) Optional<Integer> page,
			@RequestParam(value = "size", required = false) Optional<Integer> size) {
		String organizationId = getCurrentOrganizationId();
		VenueMapSearchCriteria criteria = VenueMapSearchCriteria.builder()
				.organizationId(organizationId)
				.build();
		PaginatedDto pagination = PaginatedDto.builder()
				.page(page.orElse(0))
				.size(size.orElse(10))
				.build();
		var venueMaps = venueMapService.getAllVenueMaps(criteria, pagination);
		if(venueMaps != null && !venueMaps.isEmpty()) {
			return ResponseEntity.ok(venueMaps);
		}
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{" + ID_PARAM + "}")
	public ResponseEntity<?> getVenueMapById(@PathVariable(ID_PARAM) String id) {
		var venueMap = venueMapService.getVenueMapById(id);
		if(venueMap != null) {
			return ResponseEntity.ok(venueMap);
		}
		return ResponseEntity.notFound().build();
	}

	@PostMapping
	public ResponseEntity<?> createVenueMap(@RequestBody VenueMapRequestDto venueMapRequestDto) {
		var createdVenueMap = venueMapService.createVenueMap(venueMapRequestDto);
		if(createdVenueMap != null) {
			return ResponseEntity.status(201).body(createdVenueMap);
		}
		return ResponseEntity.badRequest().build();
	}

	@PutMapping("/{" + ID_PARAM + "}")
	public ResponseEntity<?> updateVenueMap(@PathVariable(ID_PARAM) String id,
			@RequestBody VenueMapRequestDto venueMapRequestDto) {
		var updatedVenueMap = venueMapService.updateVenueMap(id, venueMapRequestDto);
		if(updatedVenueMap != null) {
			return ResponseEntity.ok(updatedVenueMap);
		}
		return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{" + ID_PARAM + "}")
	public ResponseEntity<?> deleteVenueMap(@PathVariable(ID_PARAM) String id) {
		try {
			venueMapService.deleteVenueMap(id);
			return ResponseEntity.noContent().build();
		} catch(Exception e) {
			return ResponseEntity.notFound().build();
		}
	}
}
