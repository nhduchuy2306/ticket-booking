package com.gyp.common.exceptions;

import java.io.Serial;

public class TransactionException extends RuntimeException {
	@Serial
	private static final long serialVersionUID = -1082659720262181674L;

	public TransactionException() {
	}

	public TransactionException(String message) {
		super(message);
	}

	public TransactionException(String message, Throwable cause) {
		super(message, cause);
	}

	public TransactionException(Throwable cause) {
		super(cause);
	}

	public TransactionException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
