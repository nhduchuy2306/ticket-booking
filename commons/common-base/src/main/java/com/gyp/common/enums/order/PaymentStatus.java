package com.gyp.common.enums.order;

import lombok.Getter;

@Getter
public enum PaymentStatus {
	SUCCESS("Success"),
	FAILED("Failed");

	private final String displayName;

	PaymentStatus(String displayName) {
		this.displayName = displayName;
	}
}