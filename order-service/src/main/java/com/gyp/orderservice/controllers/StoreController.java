package com.gyp.orderservice.controllers;

import java.util.Map;

import com.gyp.orderservice.services.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(StoreController.STORE_CONTROLLER_RESOURCE_PATH)
public class StoreController {
	public static final String STORE_CONTROLLER_RESOURCE_PATH = "stores";

	private final StoreService storeService;

	@PostMapping("/countdown-session")
	public ResponseEntity<?> createCountDownSession() {
		String sessionId = "session-" + System.currentTimeMillis();
		Integer duration = storeService.createCountDownSession(sessionId);
		return ResponseEntity.ok(Map.of(
				"duration", duration,
				"sessionId", sessionId
		));
	}

	@GetMapping("/countdown-session/{sessionId}")
	public ResponseEntity<?> getCountDownSession(@PathVariable("sessionId") String sessionId) {
		Integer remaining = storeService.getCountDownSession(sessionId);
		return ResponseEntity.ok(Map.of(
				"status", remaining != null ? "active" : "expired",
				"duration", remaining != null ? remaining : 0
		));
	}
}
