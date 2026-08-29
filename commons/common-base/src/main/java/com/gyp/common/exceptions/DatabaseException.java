package com.gyp.common.exceptions;

import java.io.Serial;

public class DatabaseException extends RuntimeException {
	@Serial
	private static final long serialVersionUID = -5610750812210248300L;

	public DatabaseException() {
	}

	public DatabaseException(String message) {
		super(message);
	}

	public DatabaseException(String message, Throwable cause) {
		super(message, cause);
	}

	public DatabaseException(Throwable cause) {
		super(cause);
	}

	public DatabaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
