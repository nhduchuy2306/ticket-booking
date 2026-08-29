package com.gyp.common.exceptions;

import java.io.Serial;

public class SystemException extends RuntimeException {
	@Serial
	private static final long serialVersionUID = -4357700157649331320L;

	public SystemException() {
	}

	public SystemException(String message) {
		super(message);
	}

	public SystemException(String message, Throwable cause) {
		super(message, cause);
	}

	public SystemException(Throwable cause) {
		super(cause);
	}

	public SystemException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
