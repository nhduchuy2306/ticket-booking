package com.gyp.common.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ValidationController.VALIDATION_CONTROLLER_PATH)
public class ValidationController {
	public static final String VALIDATION_CONTROLLER_PATH = "validate";
}
