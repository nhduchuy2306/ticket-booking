package com.gyp.common.dtos.validation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidationError {
	private String field;
	private String message;
	private String errorCode;
	private Object rejectedValue;
}
