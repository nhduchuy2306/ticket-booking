package com.gyp.common.dtos.validation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidationError {
	private String field;
	private String message;
	private String errorCode;
	private Object rejectedValue;
}
