package com.gyp.common.services;

import java.util.List;
import java.util.Map;

public interface DataIntegrityService {
	Map<String, List<Map<String, Object>>> findReferences(String scheme, String tableName, String id);

	boolean isDuplicate(Class<?> clazz, Map<String, Object> fieldValues, Object excludeId);
}
