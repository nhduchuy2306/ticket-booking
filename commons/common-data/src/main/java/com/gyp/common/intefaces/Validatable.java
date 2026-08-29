package com.gyp.common.intefaces;

import com.gyp.common.validators.criteria.ValidationInfo;

public interface Validatable {
	ValidationInfo validate(Class<?> clazz);
}
