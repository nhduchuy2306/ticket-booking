package com.gyp.authservice.exceptions;

import java.io.Serial;

public class ReferenceException extends RuntimeException {
	@Serial
	private static final long serialVersionUID = 8154634507268933267L;

	public ReferenceException() {
		super();
	}

	public ReferenceException(String message) {
		super(message);
	}

	public ReferenceException(String message, Throwable cause) {
		super(message, cause);
	}
}
