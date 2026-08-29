package com.gyp.common.controllers;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

public abstract class AbstractValidatableController extends AbstractController {
	public abstract ResponseEntity<?> getValidationDetails();

	public abstract ResponseEntity<?> getReferences(String id);

	protected ResponseEntity<?> createValidationResponse(Class<?> dtoClass, Object validationResult) {
		Map<String, Object> response = new HashMap<>();
		response.put("validationClass", dtoClass.getSimpleName());
		response.put("validationResult", validationResult);
		response.put("timestamp", LocalDateTime.now());
		response.put("validatedBy", getCurrentUser());
		return ResponseEntity.ok(response);
	}

	protected ResponseEntity<?> createValidationErrorResponse(String message, List<String> errors) {
		Map<String, Object> response = new HashMap<>();
		response.put("success", false);
		response.put("message", message);
		response.put("validationErrors", errors);
		response.put("timestamp", LocalDateTime.now());
		return ResponseEntity.badRequest().body(response);
	}
}
