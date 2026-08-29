package com.gyp.common.validators.rulecheck;

import java.util.Map;

import com.gyp.common.services.DataIntegrityService;
import com.gyp.common.validators.criteria.DuplicateCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class CheckFactory {
	private final DataIntegrityService dataIntegrityService;

	public CheckDuplicate createSingleFieldDuplicateCheck(Class<?> clazz, String fieldName, Object value,
			Object excludeId) {
		Map<String, Object> fieldValues = Map.of(fieldName, value);
		return new CheckDuplicate(new DuplicateCriteria(clazz, fieldValues, excludeId), dataIntegrityService);
	}

	public CheckDuplicate createMultiFieldDuplicateCheck(Class<?> clazz, Map<String, Object> fieldValues) {
		return new CheckDuplicate(new DuplicateCriteria(clazz, fieldValues), dataIntegrityService);
	}

	public CheckDuplicate createMultiFieldDuplicateCheck(Class<?> clazz, Map<String, Object> fieldValues,
			Object excludeId) {
		return new CheckDuplicate(new DuplicateCriteria(clazz, fieldValues, excludeId), dataIntegrityService);
	}
}
