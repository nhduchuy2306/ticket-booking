package com.gyp.common.enums.order;

import lombok.Getter;

@Getter
public enum OrderStatus {
	PENDING("Pending"),
	PROCESSING("Processing"),
	CANCELLED("Cancelled"),
	ERROR("Error"),
	DONE("Done");

	private final String displayName;

	OrderStatus(String displayName) {
		this.displayName = displayName;
	}
}
