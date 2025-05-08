package com.gyp.eventservice.controllers;

import com.gyp.eventservice.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(EventController.EVENT_CONTROLLER_RESOURCE_PATH)
public class EventController {
	public static final String EVENT_CONTROLLER_RESOURCE_PATH = "/events";

	private final EventService eventService;


}
