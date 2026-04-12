package com.gyp.eventservice.controllers;

import com.gyp.common.controllers.AbstractController;
import com.gyp.eventservice.dtos.eventsectionmapping.EventSectionMappingListRequestDto;
import com.gyp.eventservice.services.EventSectionMappingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(EventSectionMappingController.EVENT_SECTION_MAPPINGS_RESOURCE_PATH)
public class EventSectionMappingController extends AbstractController {
	public static final String EVENT_SECTION_MAPPINGS_RESOURCE_PATH = "/event-section-mappings";

	private final EventSectionMappingService eventSectionMappingService;

	@GetMapping("/{eventId}")
	@PreAuthorize("@permissionEvaluator.hasPermission(authentication, #AppPerm.EVENT, #ActionPerm.READ)")
	public ResponseEntity<?> getAllEventSectionMappingsByEventId(@PathVariable("eventId") String eventId) {
		return ResponseEntity.ok(eventSectionMappingService.getAllEventSectionMappingsForEvent(eventId));
	}

	@PostMapping
	@PreAuthorize("@permissionEvaluator.hasPermission(authentication, #AppPerm.EVENT, #ActionPerm.CREATE)")
	public ResponseEntity<?> createEventSectionMapping(@RequestBody EventSectionMappingListRequestDto request) {
		return ResponseEntity.ok(eventSectionMappingService.createEventSectionMappingsForEvent(
				request.getEventSectionMappingRequestDtos()));
	}

	@PutMapping
	@PreAuthorize("@permissionEvaluator.hasPermission(authentication, #AppPerm.EVENT, #ActionPerm.UPDATE)")
	public ResponseEntity<?> updateEventSectionMapping(@RequestBody EventSectionMappingListRequestDto request) {
		return ResponseEntity.ok(eventSectionMappingService.updateEventSectionMappingsForEvent(
				request.getEventSectionMappingRequestDtos()));
	}
}
