package com.gyp.eventservice.exceptions;

import java.io.Serial;

public class CategoryNotFoundException extends Exception {
	@Serial
	private static final long serialVersionUID = 2278076051939291903L;

	public CategoryNotFoundException(String message) {
		super("Category not found: " + message);
	}
}
