package com.gyp.common.utils;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public final class ParseUtils {
	public static UUID parseUUID(String id) {
		try {
			return UUID.fromString(id);
		} catch(IllegalArgumentException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid UUID format: " + id);
		}
	}

	public static Long parseLong(String value, String fieldName) {
		try {
			return Long.parseLong(value);
		} catch(NumberFormatException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Invalid number format for " + fieldName + ": " + value);
		}
	}

	public static Integer parseInteger(String value, String fieldName) {
		try {
			return Integer.parseInt(value);
		} catch(NumberFormatException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Invalid integer format for " + fieldName + ": " + value);
		}
	}

	public static LocalDateTime parseDateTime(String dateTime, String fieldName) {
		try {
			return LocalDateTime.parse(dateTime);
		} catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Invalid datetime format for " + fieldName + ": " + dateTime);
		}
	}
}
