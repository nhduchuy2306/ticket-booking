package com.gyp.eventservice.exceptions;

import java.io.Serial;

public class VenueNotFoundException extends Exception {
	@Serial
	private static final long serialVersionUID = 8204186227819303859L;

	public VenueNotFoundException(String message) {
		super("Venue not found: " + message);
	}

	public VenueNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
