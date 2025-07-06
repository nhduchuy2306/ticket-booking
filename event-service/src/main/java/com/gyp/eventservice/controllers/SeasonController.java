package com.gyp.eventservice.controllers;

import com.gyp.common.controllers.AbstractValidatableController;
import com.gyp.common.services.DataIntegrityService;
import com.gyp.eventservice.dtos.season.SeasonRequestDto;
import com.gyp.eventservice.services.SeasonService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
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
@RequestMapping(SeasonController.SEASON_CONTROLLER_RESOURCE_PATH)
public class SeasonController extends AbstractValidatableController {
	public static final String SEASON_CONTROLLER_RESOURCE_PATH = "/seasons";

	private final SeasonService seasonService;
	private final DataIntegrityService dataIntegrityService;

	@GetMapping
	public ResponseEntity<?> getAllSeasons() {
		return ResponseEntity.ok(seasonService.getAllSeasons());
	}

	@GetMapping("/{" + ID_PARAM + "}")
	public ResponseEntity<?> getActiveSeason(@PathVariable(ID_PARAM) String seasonId) {
		return ResponseEntity.ok(seasonService.getSeasonById(seasonId));
	}

	@GetMapping("/active")
	public ResponseEntity<?> getActiveSeason() {
		return ResponseEntity.ok(seasonService.getActiveSeason());
	}

	@PostMapping
	public ResponseEntity<?> createSeason(@RequestBody SeasonRequestDto seasonRequestDto) {
		return ResponseEntity.ok(seasonService.createSeason(seasonRequestDto));
	}

	@PutMapping("/{" + ID_PARAM + "}")
	public ResponseEntity<?> updateSeason(@PathVariable(ID_PARAM) String seasonId,
			@RequestBody SeasonRequestDto seasonRequestDto) {
		return ResponseEntity.ok(seasonService.updateSeason(seasonId, seasonRequestDto));
	}

	@DeleteMapping("/{" + ID_PARAM + "}")
	public ResponseEntity<?> deleteSeason(@PathVariable(ID_PARAM) String seasonId) {
		seasonService.deleteSeason(seasonId);
		return ResponseEntity.noContent().build();
	}

	@Override
	@GetMapping("/{" + ID_PARAM + "}" + REFERENCES_PATH)
	public ResponseEntity<?> getReferences(@PathVariable("id") String id) {
		if(StringUtils.isEmpty(id)) {
			return ResponseEntity.badRequest().body("Season ID must not be null or empty");
		}
		var references = dataIntegrityService.findReferences(
				"event_service",
				"season",
				id
		);
		return ResponseEntity.ok(references);
	}

	@Override
	@GetMapping(VALIDATION_PATH)
	public ResponseEntity<?> getValidationDetails() {
		return ResponseEntity.ok(seasonService.validate(SeasonRequestDto.class));
	}
}
