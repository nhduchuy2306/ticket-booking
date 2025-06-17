package com.gyp.eventservice.exceptions;

import java.io.Serial;

public class EventNotFoundException extends Exception {
	@Serial
	private static final long serialVersionUID = 7972049745853104745L;

	public EventNotFoundException(String message) {
		super("Event not found: " + message);
	}
}
