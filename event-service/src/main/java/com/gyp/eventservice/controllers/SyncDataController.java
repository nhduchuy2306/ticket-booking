package com.gyp.eventservice.controllers;

import com.gyp.common.controllers.AbstractController;
import com.gyp.eventservice.messages.producers.EventProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(SyncDataController.SYNC_DATA_CONTROLLER_PATH)
public class SyncDataController extends AbstractController {
	public static final String SYNC_DATA_CONTROLLER_PATH = "syncdatas";
	private static final String SYNC_EVENT_PATH = "syncevent";

	private final EventProducer eventProducer;

	@GetMapping(SYNC_EVENT_PATH)
	public ResponseEntity<?> syncEvent() {
		eventProducer.syncEvent();
		return ResponseEntity.ok("Sync event data successfully");
	}
}
