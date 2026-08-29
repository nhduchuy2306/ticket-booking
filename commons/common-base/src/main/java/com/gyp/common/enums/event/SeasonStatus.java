package com.gyp.common.enums.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SeasonStatus {
	ACTIVE("active"),
	INACTIVE("inactive");

	private final String status;

	public static SeasonStatus fromString(String status) {
		for(SeasonStatus seasonStatus : SeasonStatus.values()) {
			if(seasonStatus.status.equalsIgnoreCase(status)) {
				return seasonStatus;
			}
		}
		throw new IllegalArgumentException("Unknown season status: " + status);
	}
}
