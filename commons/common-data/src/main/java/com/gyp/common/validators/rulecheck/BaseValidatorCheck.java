package com.gyp.common.validators.rulecheck;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class BaseValidatorCheck<T> {
	protected final T criteria;

	public abstract boolean isValid();

	public abstract String getErrorMessage();

	public abstract String getFieldName();

	public abstract String getErrorCode();

	public abstract String getErrorDescription();
}
