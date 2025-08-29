package com.gyp.common.enums.event;

public enum EventStatus {
	DRAFT,
	ON_SALE,
	PENDING_APPROVAL,
	PUBLISHED,
	CANCELLED,
	POSTPONED,
	COMPLETED;

	public boolean isPublished() {
		return equals(PUBLISHED);
	}
}
