package com.gyp.common.services;

import com.gyp.common.validators.criteria.ValidationInfo;

public interface ValidationService {
	ValidationInfo extractValidationInfo(Class<?> clazz);
}
