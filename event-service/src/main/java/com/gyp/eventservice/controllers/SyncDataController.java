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
	private static final String SYNC_USER_ACCOUNT_PATH = "syncuseraccount";

	private final EventProducer eventProducer;

	@GetMapping(SYNC_USER_ACCOUNT_PATH)
	public ResponseEntity<?> syncUserAccount() {
		eventProducer.syncEvent();
		return ResponseEntity.ok("Sync event data successfully");
	}
}
