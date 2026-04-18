package com.gyp.eventservice.controllers;

import com.gyp.common.controllers.AbstractController;
import com.gyp.eventservice.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(EventCustomerController.EVENT_CUSTOMER_CONTROLLER_RESOURCE_PATH)
public class EventCustomerController extends AbstractController {
	public static final String EVENT_CUSTOMER_CONTROLLER_RESOURCE_PATH = "/customers/events";

	private final EventService eventService;

	@GetMapping("/{" + ID_PARAM + "}")
	public ResponseEntity<?> getEventById(@PathVariable(ID_PARAM) String id) {
		return ResponseEntity.ok(eventService.getEventById(id));
	}
}
