package com.gyp.common.exceptions;

import java.io.Serial;

public class ResourceDuplicateException extends RuntimeException {
	@Serial
	private static final long serialVersionUID = 5416468085864373160L;

	public ResourceDuplicateException(String message) {
		super(message);
	}

	public ResourceDuplicateException(String message, Throwable cause) {
		super(message, cause);
	}

	public ResourceDuplicateException(Throwable cause) {
		super(cause);
	}

	public ResourceDuplicateException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
