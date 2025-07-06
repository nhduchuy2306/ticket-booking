package com.gyp.common.controllers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

public abstract class AbstractController {
	// Constants
	protected static final String INTERNAL_API = "/internal";
	protected static final String ID_PARAM = "id";
	protected static final String VALIDATION_PATH = "/validation";
	protected static final String REFERENCES_PATH = "/references";
	protected static final String DEFAULT_PAGE_SIZE = "20";
	protected static final String DEFAULT_PAGE_NUMBER = "0";

	// Authentication & Authorization
	protected String getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication == null || !authentication.isAuthenticated()) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
		}
		return authentication.getName();
	}

	protected Authentication getCurrentAuthentication() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication == null || !authentication.isAuthenticated()) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
		}
		return authentication;
	}

	protected boolean hasRole(String role) {
		Authentication auth = getCurrentAuthentication();
		return auth.getAuthorities().stream()
				.anyMatch(authority -> authority.getAuthority().equals("ROLE_" + role));
	}

	protected boolean hasAnyRole(String... roles) {
		Authentication auth = getCurrentAuthentication();
		return Arrays.stream(roles)
				.anyMatch(role -> auth.getAuthorities().stream()
						.anyMatch(authority -> authority.getAuthority().equals("ROLE_" + role)));
	}

	protected void requireRole(String role) {
		if(!hasRole(role)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Insufficient privileges");
		}
	}

	// Parsing & Validation
	protected UUID parseUUID(String id) {
		try {
			return UUID.fromString(id);
		} catch(IllegalArgumentException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid UUID format: " + id);
		}
	}

	protected Long parseLong(String value, String fieldName) {
		try {
			return Long.parseLong(value);
		} catch(NumberFormatException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Invalid number format for " + fieldName + ": " + value);
		}
	}

	protected Integer parseInteger(String value, String fieldName) {
		try {
			return Integer.parseInt(value);
		} catch(NumberFormatException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Invalid integer format for " + fieldName + ": " + value);
		}
	}

	protected LocalDateTime parseDateTime(String dateTime, String fieldName) {
		try {
			return LocalDateTime.parse(dateTime);
		} catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Invalid datetime format for " + fieldName + ": " + dateTime);
		}
	}

	// Response Builders
	public ResponseEntity<?> createResponseOk(Object data) {
		return ResponseEntity.ok(data);
	}

	public ResponseEntity<?> createResponseCreated(Object data) {
		return ResponseEntity.status(HttpStatus.CREATED).body(data);
	}

	public ResponseEntity<?> createResponseNoContent() {
		return ResponseEntity.noContent().build();
	}

	public ResponseEntity<?> createResponseAccepted(Object data) {
		return ResponseEntity.accepted().body(data);
	}

	public ResponseEntity<?> createResponseBadRequest(String message) {
		return ResponseEntity.badRequest().body(Map.of("error", message, "timestamp", LocalDateTime.now()));
	}

	public ResponseEntity<?> createResponseNotFound(String message) {
		return ResponseEntity.notFound().build();
	}

	public ResponseEntity<?> createResponseForbidden(String message) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN)
				.body(Map.of("error", message, "timestamp", LocalDateTime.now()));
	}

	public ResponseEntity<?> createResponseUnauthorized(String message) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(Map.of("error", message, "timestamp", LocalDateTime.now()));
	}

	public ResponseEntity<?> createResponseConflict(String message) {
		return ResponseEntity.status(HttpStatus.CONFLICT)
				.body(Map.of("error", message, "timestamp", LocalDateTime.now()));
	}

	// Paginated Response
	public ResponseEntity<?> createPagedResponse(Page<?> page) {
		Map<String, Object> response = new HashMap<>();
		response.put("content", page.getContent());
		response.put("page", Map.of(
				"number", page.getNumber(),
				"size", page.getSize(),
				"totalElements", page.getTotalElements(),
				"totalPages", page.getTotalPages(),
				"first", page.isFirst(),
				"last", page.isLast(),
				"hasNext", page.hasNext(),
				"hasPrevious", page.hasPrevious()
		));
		return ResponseEntity.ok(response);
	}

	// Common Exception Handlers
	protected ResponseEntity<?> handleNotFoundException(String entityName, Object id) {
		return createResponseNotFound(entityName + " not found with id: " + id);
	}

	protected ResponseEntity<?> handleValidationException(String message) {
		return createResponseBadRequest("Validation failed: " + message);
	}

	protected ResponseEntity<?> handleDuplicateException(String entityName, String field, Object value) {
		return createResponseConflict(entityName + " already exists with " + field + ": " + value);
	}

	// Request Validation
	protected void validateNotNull(Object value, String fieldName) {
		if(value == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, fieldName + " cannot be null");
		}
	}

	protected void validateNotEmpty(String value, String fieldName) {
		if(value == null || value.trim().isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, fieldName + " cannot be empty");
		}
	}

	protected void validatePositive(Number value, String fieldName) {
		if(value == null || value.doubleValue() <= 0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, fieldName + " must be positive");
		}
	}

	protected Map<String, Object> createSuccessResponse(String message, Object data) {
		Map<String, Object> response = new HashMap<>();
		response.put("success", true);
		response.put("message", message);
		response.put("data", data);
		response.put("timestamp", LocalDateTime.now());
		return response;
	}

	protected Map<String, Object> createErrorResponse(String message, String code) {
		Map<String, Object> response = new HashMap<>();
		response.put("success", false);
		response.put("message", message);
		response.put("errorCode", code);
		response.put("timestamp", LocalDateTime.now());
		return response;
	}
}
