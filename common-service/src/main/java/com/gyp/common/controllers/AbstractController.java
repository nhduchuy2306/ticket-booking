package com.gyp.common.controllers;

import java.util.UUID;
import java.util.function.Supplier;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

public abstract class AbstractController {
	protected static final String INTERNAL_API = "/internal";
	protected static final String ID_PARAM = "id";

	protected String getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication == null || !authentication.isAuthenticated()) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
		}
		return authentication.getName();
	}

	protected UUID parseUUID(String id) {
		try {
			return UUID.fromString(id);
		} catch(IllegalArgumentException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid UUID format: " + id);
		}
	}

	public ResponseEntity<?> createResponseOk(Object data) {
		return ResponseEntity.ok(data);
	}

	public ResponseEntity<?> createResponseCreated(Object data) {
		return ResponseEntity.status(HttpStatus.CREATED).body(data);
	}
}
