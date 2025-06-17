package com.gyp.eventservice.services;

public interface EventNotificationService {
	void notifyEventCreated(String eventId);

	void notifyEventPublished(String eventId);

	void notifyEventCancelled(String eventId);

	void notifyEventApproved(String eventId);

	void notifyEventRejected(String eventId, String reason);
}
