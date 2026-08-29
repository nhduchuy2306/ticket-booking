package com.gyp.common.exceptions;

import java.io.Serial;

public class UnAuthorizationException extends RuntimeException {
	@Serial
	private static final long serialVersionUID = -6602416588138499609L;

	public UnAuthorizationException() {
	}

	public UnAuthorizationException(String message) {
		super(message);
	}

	public UnAuthorizationException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnAuthorizationException(Throwable cause) {
		super(cause);
	}

	public UnAuthorizationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
