package com.gyp.common.exceptions;

import java.io.Serial;

public class DataIntegrityException extends RuntimeException {
	@Serial
	private static final long serialVersionUID = 5160520691256470582L;

	public DataIntegrityException() {
	}

	public DataIntegrityException(String message) {
		super(message);
	}

	public DataIntegrityException(String message, Throwable cause) {
		super(message, cause);
	}

	public DataIntegrityException(Throwable cause) {
		super(cause);
	}

	public DataIntegrityException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
