package com.gyp.eventservice.controllers;

import java.util.Optional;

import com.gyp.common.controllers.AbstractController;
import com.gyp.common.dtos.pagination.PaginatedDto;
import com.gyp.eventservice.dtos.season.SeasonRequestDto;
import com.gyp.eventservice.services.SeasonService;
import com.gyp.eventservice.services.criteria.SeasonSearchCriteria;
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
@RequestMapping(SeasonController.SEASON_CONTROLLER_RESOURCE_PATH)
public class SeasonController extends AbstractController {
	public static final String SEASON_CONTROLLER_RESOURCE_PATH = "/seasons";

	private final SeasonService seasonService;

	@GetMapping
	@PreAuthorize("@permissionEvaluator.hasPermission(authentication, #AppPerm.SEASON, #ActionPerm.READ)")
	public ResponseEntity<?> getAllSeasons(
			@RequestParam(value = "page", required = false) Optional<Integer> page,
			@RequestParam(value = "size", required = false) Optional<Integer> size) {
		String organizationId = getCurrentOrganizationId();
		SeasonSearchCriteria criteria = SeasonSearchCriteria.builder()
				.organizationId(organizationId)
				.build();
		PaginatedDto pagination = PaginatedDto.builder()
				.page(page.orElse(0))
				.size(size.orElse(10))
				.build();
		return ResponseEntity.ok(seasonService.getAllSeasons(criteria, pagination));
	}

	@GetMapping("/{" + ID_PARAM + "}")
	@PreAuthorize("@permissionEvaluator.hasPermission(authentication, #AppPerm.SEASON, #ActionPerm.READ)")
	public ResponseEntity<?> getActiveSeason(@PathVariable(ID_PARAM) String seasonId) {
		return ResponseEntity.ok(seasonService.getSeasonById(seasonId));
	}

	@GetMapping("/active")
	@PreAuthorize("@permissionEvaluator.hasPermission(authentication, #AppPerm.SEASON, #ActionPerm.READ)")
	public ResponseEntity<?> getActiveSeason() {
		return ResponseEntity.ok(seasonService.getActiveSeason());
	}

	@PostMapping
	@PreAuthorize("@permissionEvaluator.hasPermission(authentication, #AppPerm.SEASON, #ActionPerm.CREATE)")
	public ResponseEntity<?> createSeason(@RequestBody SeasonRequestDto seasonRequestDto) {
		return ResponseEntity.ok(seasonService.createSeason(seasonRequestDto));
	}

	@PutMapping("/{" + ID_PARAM + "}")
	@PreAuthorize("@permissionEvaluator.hasPermission(authentication, #AppPerm.SEASON, #ActionPerm.UPDATE)")
	public ResponseEntity<?> updateSeason(@PathVariable(ID_PARAM) String seasonId,
			@RequestBody SeasonRequestDto seasonRequestDto) {
		return ResponseEntity.ok(seasonService.updateSeason(seasonId, seasonRequestDto));
	}

	@DeleteMapping("/{" + ID_PARAM + "}")
	@PreAuthorize("@permissionEvaluator.hasPermission(authentication, #AppPerm.SEASON, #ActionPerm.DELETE)")
	public ResponseEntity<?> deleteSeason(@PathVariable(ID_PARAM) String seasonId) {
		seasonService.deleteSeason(seasonId);
		return ResponseEntity.noContent().build();
	}
}
