package com.gyp.eventservice.controllers;

import com.gyp.common.annotations.RequestPermission;
import com.gyp.common.enums.permission.ActionPermission;
import com.gyp.common.enums.permission.ApplicationPermission;
import com.gyp.eventservice.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(EventController.EVENT_CONTROLLER_RESOURCE_PATH)
public class EventController {
	public static final String EVENT_CONTROLLER_RESOURCE_PATH = "/events";

	private final EventService eventService;

	@GetMapping
	@RequestPermission(application = ApplicationPermission.EVENT, actions = { ActionPermission.READ })
	public ResponseEntity<?> getAllEvents() {
		return ResponseEntity.ok(eventService.getAllEvents());
	}
}
