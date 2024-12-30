package com.gyp.common.exceptions;

import java.io.Serial;

public class AccessDeniedException extends RuntimeException {
	@Serial
	private static final long serialVersionUID = -6602416588138499609L;

	public AccessDeniedException(String message) {
		super(message);
	}
}
