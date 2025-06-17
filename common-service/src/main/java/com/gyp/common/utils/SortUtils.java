package com.gyp.common.utils;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

public final class SortUtils {
	private SortUtils() {
	}

	public static Sort buildSort(String sortDirection, String... sortFields) {
		if(sortDirection == null || sortDirection.isEmpty()) {
			sortDirection = Sort.Direction.ASC.name();
		}
		return Sort.by(
				Direction.DESC.name().equalsIgnoreCase(sortDirection) ?
						Sort.Direction.DESC : Sort.Direction.ASC,
				sortFields
		);
	}
}
