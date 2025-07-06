package com.gyp.eventservice.controllers;

import jakarta.validation.Valid;

import com.gyp.common.annotations.RequestPermission;
import com.gyp.common.controllers.AbstractValidatableController;
import com.gyp.common.enums.permission.ActionPermission;
import com.gyp.common.enums.permission.ApplicationPermission;
import com.gyp.common.services.DataIntegrityService;
import com.gyp.eventservice.dtos.event.EventRequestDto;
import com.gyp.eventservice.services.EventService;
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
@RequestMapping(EventController.EVENT_CONTROLLER_RESOURCE_PATH)
public class EventController extends AbstractValidatableController {
	public static final String EVENT_CONTROLLER_RESOURCE_PATH = "/events";

	private final EventService eventService;
	private final DataIntegrityService dataIntegrityService;

	@GetMapping
	@RequestPermission(application = ApplicationPermission.EVENT, actions = { ActionPermission.READ })
	public ResponseEntity<?> getAllEvents() {
		return ResponseEntity.ok(eventService.getAllEvents());
	}

	@GetMapping("/{" + ID_PARAM + "}")
	@RequestPermission(application = ApplicationPermission.EVENT, actions = { ActionPermission.READ })
	public ResponseEntity<?> getEventById(@PathVariable(ID_PARAM) String id) {
		return ResponseEntity.ok(eventService.getEventById(id));
	}

	@PostMapping
	public ResponseEntity<?> createEvent(@RequestBody @Valid EventRequestDto eventRequestDto) {
		return ResponseEntity.ok(eventService.createEvent(eventRequestDto));
	}

	@PutMapping("/{" + ID_PARAM + "}")
	public ResponseEntity<?> updateEvent(@PathVariable(ID_PARAM) String id,
			@RequestBody @Valid EventRequestDto eventRequestDto) {
		return ResponseEntity.ok(eventService.updateEvent(id, eventRequestDto));
	}

	@DeleteMapping("/{" + ID_PARAM + "}")
	public ResponseEntity<?> deleteEvent(@PathVariable(ID_PARAM) String id) {
		eventService.deleteEvent(id);
		return ResponseEntity.noContent().build();
	}

	@Override
	@GetMapping("/{" + ID_PARAM + "}/" + REFERENCES_PATH)
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
	@GetMapping(VALIDATION_PATH)
	public ResponseEntity<?> getValidationDetails() {
		return ResponseEntity.ok(eventService.validate(EventRequestDto.class));
	}
}
