package com.gyp.common.exceptions;

import java.io.Serial;

public class QRLogoImageNotFoundException extends RuntimeException {
	@Serial
	private static final long serialVersionUID = 6266356488991588710L;

	public QRLogoImageNotFoundException() {
	}

	public QRLogoImageNotFoundException(String message) {
		super(message);
	}

	public QRLogoImageNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public QRLogoImageNotFoundException(Throwable cause) {
		super(cause);
	}

	public QRLogoImageNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
