package com.gyp.common.exceptions;

import java.io.Serial;

public class ValidationException extends RuntimeException {
	@Serial
	private static final long serialVersionUID = -3262257724737098456L;
	private final String errorCode;
	private final String fieldName;

	public ValidationException(String errorCode, String message, String fieldName) {
		super(message);
		this.errorCode = errorCode;
		this.fieldName = fieldName;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public String getFieldName() {
		return fieldName;
	}
}
