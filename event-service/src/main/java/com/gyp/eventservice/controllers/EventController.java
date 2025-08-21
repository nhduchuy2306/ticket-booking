package com.gyp.eventservice.controllers;

import java.util.Optional;

import jakarta.validation.Valid;

import com.gyp.common.controllers.AbstractValidatableController;
import com.gyp.common.dtos.pagination.PaginatedDto;
import com.gyp.common.services.DataIntegrityService;
import com.gyp.eventservice.dtos.event.EventRequestDto;
import com.gyp.eventservice.services.EventService;
import com.gyp.eventservice.services.criteria.EventSearchCriteria;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping(EventController.EVENT_CONTROLLER_RESOURCE_PATH)
public class EventController extends AbstractValidatableController {
	public static final String EVENT_CONTROLLER_RESOURCE_PATH = "/events";

	private static final String ACTIVE_PATH = "/active";
	private static final String WITH_UPLOAD_PATH = "/with-upload";

	private final EventService eventService;
	private final DataIntegrityService dataIntegrityService;

	@GetMapping
	@PreAuthorize(
			"@permissionEvaluator.hasPermission(authentication, #AppPerm.EVENT.getApplicationId(), #ActionPerm.READ.name())")
	public ResponseEntity<?> getAllEvents(
			@RequestParam(value = "page", required = false) Optional<Integer> page,
			@RequestParam(value = "size", required = false) Optional<Integer> size) {
		String organizationId = getCurrentOrganizationId();
		EventSearchCriteria criteria = EventSearchCriteria.builder()
				.organizationId(organizationId)
				.build();
		PaginatedDto pagination = PaginatedDto.builder()
				.page(page.orElse(0))
				.size(size.orElse(10))
				.build();
		return ResponseEntity.ok(eventService.getAllEvents(criteria, pagination));
	}

	@GetMapping("/{" + ID_PARAM + "}")
	@PreAuthorize(
			"@permissionEvaluator.hasPermission(authentication, #AppPerm.EVENT.getApplicationId(), #ActionPerm.READ.name())")
	public ResponseEntity<?> getEventById(@PathVariable(ID_PARAM) String id) {
		return ResponseEntity.ok(eventService.getEventById(id));
	}

	@GetMapping(ACTIVE_PATH)
	@PreAuthorize(
			"@permissionEvaluator.hasPermission(authentication, #AppPerm.EVENT.getApplicationId(), #ActionPerm.READ.name())")
	public ResponseEntity<?> getAllActiveEvents() {
		return ResponseEntity.ok(eventService.getAllActiveEvents());
	}

	@PostMapping
	@PreAuthorize(
			"@permissionEvaluator.hasPermission(authentication, #AppPerm.EVENT.getApplicationId(), #ActionPerm.CREATE.name())")
	public ResponseEntity<?> createEvent(@RequestBody @Valid EventRequestDto eventRequestDto) {
		return ResponseEntity.ok(eventService.createEvent(eventRequestDto));
	}

	@PostMapping(WITH_UPLOAD_PATH)
	@PreAuthorize(
			"@permissionEvaluator.hasPermission(authentication, #AppPerm.EVENT.getApplicationId(), #ActionPerm.CREATE.name())")
	public ResponseEntity<?> createEventWithUpload(
			@RequestPart(value = "event", required = false) EventRequestDto eventRequestDto,
			@RequestPart(value = "logo", required = false) MultipartFile file) {
		var event = eventService.createEvent(eventRequestDto, file);
		if(event == null) {
			return ResponseEntity.badRequest().body("Event creation failed due to missing file or event data.");
		}
		return ResponseEntity.ok(event);
	}

	@PutMapping("/{" + ID_PARAM + "}")
	@PreAuthorize(
			"@permissionEvaluator.hasPermission(authentication, #AppPerm.EVENT.getApplicationId(), #ActionPerm.UPDATE.name())")
	public ResponseEntity<?> updateEvent(@PathVariable(ID_PARAM) String id,
			@RequestBody @Valid EventRequestDto eventRequestDto) {
		return ResponseEntity.ok(eventService.updateEvent(id, eventRequestDto));
	}

	@PutMapping(WITH_UPLOAD_PATH + "/{" + ID_PARAM + "}")
	@PreAuthorize(
			"@permissionEvaluator.hasPermission(authentication, #AppPerm.EVENT.getApplicationId(), #ActionPerm.UPDATE.name())")
	public ResponseEntity<?> updateEventWithUpload(
			@PathVariable(ID_PARAM) String id,
			@RequestPart(value = "event", required = false) EventRequestDto eventRequestDto,
			@RequestPart(value = "logo", required = false) MultipartFile file) {
		var event = eventService.updateEvent(id, eventRequestDto, file);
		if(event == null) {
			return ResponseEntity.badRequest().body("Event update failed due to missing file or event data.");
		}
		return ResponseEntity.ok(event);
	}

	@DeleteMapping("/{" + ID_PARAM + "}")
	@PreAuthorize(
			"@permissionEvaluator.hasPermission(authentication, #AppPerm.EVENT.getApplicationId(), #ActionPerm.DELETE.name())")
	public ResponseEntity<?> deleteEvent(@PathVariable(ID_PARAM) String id) {
		eventService.deleteEvent(id);
		return ResponseEntity.noContent().build();
	}

	@Override
	@GetMapping("/{" + ID_PARAM + "}/" + REFERENCES_PATH)
	@PreAuthorize(
			"@permissionEvaluator.hasPermission(authentication, #AppPerm.EVENT.getApplicationId(), #ActionPerm.READ.name())")
	public ResponseEntity<?> getReferences(@PathVariable(ID_PARAM) String id) {
		if(StringUtils.isEmpty(id)) {
			return ResponseEntity.badRequest().body("Event ID must not be null or empty");
		}
		var references = dataIntegrityService.findReferences(
				"event_service",
				"season",
				id
		);
		return ResponseEntity.ok(references);
	}

	@Override
	@PreAuthorize(
			"@permissionEvaluator.hasPermission(authentication, #AppPerm.EVENT.getApplicationId(), #ActionPerm.CREATE.name())")
	@GetMapping(VALIDATION_PATH)
	public ResponseEntity<?> getValidationDetails() {
		return ResponseEntity.ok(eventService.validate(EventRequestDto.class));
	}
}
