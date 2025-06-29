package com.gyp.common.exceptions;

import java.io.Serial;

public class DuplicateResourceException extends RuntimeException {
	@Serial
	private static final long serialVersionUID = 5416468085864373160L;

	public DuplicateResourceException(String message) {
		super(message);
	}

	public DuplicateResourceException(String message, Throwable cause) {
		super(message, cause);
	}

	public DuplicateResourceException(Throwable cause) {
		super(cause);
	}

	public DuplicateResourceException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
