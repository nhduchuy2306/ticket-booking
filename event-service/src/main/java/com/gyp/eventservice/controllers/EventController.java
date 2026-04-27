package com.gyp.eventservice.controllers;

import java.time.LocalDateTime;
import java.util.Optional;

import jakarta.validation.Valid;

import com.gyp.common.controllers.AbstractController;
import com.gyp.common.dtos.pagination.PaginatedDto;
import com.gyp.eventservice.dtos.event.EventRequestDto;
import com.gyp.eventservice.services.EventService;
import com.gyp.eventservice.services.criteria.EventSearchCriteria;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping(EventController.EVENT_CONTROLLER_RESOURCE_PATH)
public class EventController extends AbstractController {
	public static final String EVENT_CONTROLLER_RESOURCE_PATH = "/events";

	private static final String ACTIVE_PATH = "/active";
	private static final String WITH_UPLOAD_PATH = "/with-upload";
	private static final String ON_SALE_PATH = "/on-sale";
	private static final String COMING_EVENTS_PATH = "/coming-events";
	private static final String CREATED_SINCE_PATH = "/created-since";
	private static final String TOMORROW_PATH = "/tomorrow";

	private final EventService eventService;

	@GetMapping
	@PreAuthorize("@permissionEvaluator.hasPermission(authentication, #AppPerm.EVENT, #ActionPerm.READ)")
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
	@PreAuthorize("@permissionEvaluator.hasPermission(authentication, #AppPerm.EVENT, #ActionPerm.READ)")
	public ResponseEntity<?> getEventById(@PathVariable(ID_PARAM) String id) {
		return ResponseEntity.ok(eventService.getEventById(id));
	}

	@GetMapping(ACTIVE_PATH)
	@PreAuthorize("@permissionEvaluator.hasPermission(authentication, #AppPerm.EVENT, #ActionPerm.READ)")
	public ResponseEntity<?> getAllActiveEvents() {
		return ResponseEntity.ok(eventService.getAllActiveEvents());
	}

	@PostMapping
	@PreAuthorize("@permissionEvaluator.hasPermission(authentication, #AppPerm.EVENT, #ActionPerm.CREATE)")
	public ResponseEntity<?> createEvent(@RequestBody @Valid EventRequestDto eventRequestDto) {
		return ResponseEntity.ok(eventService.createEvent(eventRequestDto));
	}

	@PostMapping(WITH_UPLOAD_PATH)
	@PreAuthorize("@permissionEvaluator.hasPermission(authentication, #AppPerm.EVENT, #ActionPerm.CREATE)")
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
	@PreAuthorize("@permissionEvaluator.hasPermission(authentication, #AppPerm.EVENT, #ActionPerm.UPDATE)")
	public ResponseEntity<?> updateEvent(@PathVariable(ID_PARAM) String id,
			@RequestBody @Valid EventRequestDto eventRequestDto) {
		return ResponseEntity.ok(eventService.updateEvent(id, eventRequestDto));
	}

	@PutMapping(WITH_UPLOAD_PATH + "/{" + ID_PARAM + "}")
	@PreAuthorize("@permissionEvaluator.hasPermission(authentication, #AppPerm.EVENT, #ActionPerm.UPDATE)")
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
	@PreAuthorize("@permissionEvaluator.hasPermission(authentication, #AppPerm.EVENT, #ActionPerm.DELETE)")
	public ResponseEntity<?> deleteEvent(@PathVariable(ID_PARAM) String id) {
		eventService.deleteEvent(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping(ON_SALE_PATH)
	public ResponseEntity<?> getAllEventsOnSale() {
		return ResponseEntity.ok(eventService.getAllEventsOnSale());
	}

	@GetMapping(COMING_EVENTS_PATH)
	public ResponseEntity<?> getAllComingEvents() {
		return ResponseEntity.ok(eventService.getAllComingEvents());
	}

	@GetMapping(CREATED_SINCE_PATH)
	public ResponseEntity<?> getEventsCreatedSince(@RequestParam("since") LocalDateTime since) {
		return ResponseEntity.ok(eventService.getEventsCreatedSince(since));
	}

	@GetMapping(TOMORROW_PATH)
	public ResponseEntity<?> getTomorrowEvents() {
		return ResponseEntity.ok(eventService.getTomorrowEvents());
	}
}
