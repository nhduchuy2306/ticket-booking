package com.gyp.common.utils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import com.gyp.common.constants.AppConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

public final class SortUtils {
	private SortUtils() {
	}

	public static List<Pair<String, Boolean>> getSortFields(String... sortFields) {
		if(sortFields == null || sortFields.length == 0) {
			return Collections.emptyList();
		}

		return Stream.of(sortFields)
				.map(field -> Pair.of(getFieldName(field), isAscendingSort(field)))
				.toList();
	}

	private static boolean isAscendingSort(String... sortFields) {
		if(sortFields == null || sortFields.length == 0) {
			return true;
		}
		String firstField = sortFields[0];
		return !firstField.startsWith(AppConstants.MINUS);
	}

	private static String getFieldName(String field) {
		if(StringUtils.isEmpty(field)) {
			return null;
		}
		if(field.startsWith(AppConstants.MINUS)) {
			return field.substring(1);
		}
		return field;
	}
}
