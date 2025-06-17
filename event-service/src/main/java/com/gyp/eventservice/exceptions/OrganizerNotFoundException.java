package com.gyp.eventservice.exceptions;

import java.io.Serial;

public class OrganizerNotFoundException extends RuntimeException {
	@Serial
	private static final long serialVersionUID = -731371390875856571L;

	public OrganizerNotFoundException(String organizerId) {
		super("Organizer with ID " + organizerId + " not found.");
	}
}
