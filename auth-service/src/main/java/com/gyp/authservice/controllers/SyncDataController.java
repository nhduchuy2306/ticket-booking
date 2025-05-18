package com.gyp.authservice.controllers;

import com.gyp.authservice.messages.producers.UserAccountProducer;
import com.gyp.common.controllers.AbstractController;
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

	private final UserAccountProducer userAccountProducer;

	@GetMapping(SYNC_USER_ACCOUNT_PATH)
	public ResponseEntity<?> syncUserAccount() {
		userAccountProducer.syncUserAccount();
		return ResponseEntity.ok("Sync user account data successfully");
	}
}
