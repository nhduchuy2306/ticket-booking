package com.gyp.common.validators.criteria;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DuplicateCriteria {
	private final Class<?> clazz;
	private final Map<String, Object> fieldValues;
	private final Object excludeId;

	public DuplicateCriteria(Class<?> clazz, Map<String, Object> fieldValues) {
		this(clazz, fieldValues, null);
	}
}
