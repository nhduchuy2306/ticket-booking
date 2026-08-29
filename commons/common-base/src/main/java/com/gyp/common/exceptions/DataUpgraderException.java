package com.gyp.common.exceptions;

import java.io.Serial;

public class DataUpgraderException extends RuntimeException {
	@Serial
	private static final long serialVersionUID = 4014338005868352868L;

	public DataUpgraderException() {
	}

	public DataUpgraderException(String message) {
		super(message);
	}

	public DataUpgraderException(String message, Throwable cause) {
		super(message, cause);
	}

	public DataUpgraderException(Throwable cause) {
		super(cause);
	}

	public DataUpgraderException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
