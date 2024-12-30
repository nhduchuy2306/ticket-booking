package com.example.ticket.authservice.controllers;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

public abstract class AbstractController {
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
}
