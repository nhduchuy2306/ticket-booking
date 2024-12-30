package com.gyp.common.enums.event;

public enum EventStatus {
	DRAFT,
	PENDING_APPROVAL, // If we need to wait for approval
	PUBLISHED,
	CANCELLED,
	POSTPONED,
	COMPLETED
}
