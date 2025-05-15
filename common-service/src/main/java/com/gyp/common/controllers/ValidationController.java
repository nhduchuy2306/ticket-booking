package com.gyp.common.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(ValidationController.VALIDATION_CONTROLLER_PATH)
public class ValidationController {
	public static final String VALIDATION_CONTROLLER_PATH = "validate";

	private static final String VALIDATION_RULE_PATH = "validationrules";

	@PostMapping(VALIDATION_RULE_PATH)
	public ResponseEntity<?> getValidationRules(@RequestBody String requestJsonType) {
		return null;
	}
}
