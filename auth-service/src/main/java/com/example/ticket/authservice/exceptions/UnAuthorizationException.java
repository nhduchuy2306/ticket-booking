package com.example.ticket.authservice.exceptions;

import java.io.Serial;

public class UnAuthorizationException extends RuntimeException {
	@Serial
	private static final long serialVersionUID = -6602416588138499609L;

	public UnAuthorizationException(String message) {
		super(message);
	}
}
