package com.gyp.common.kafkatopics;

public final class EventServiceTopic {
	private EventServiceTopic() {
	}

	public static final String EVENT_SERVICE_PREFIX = "event";
	public static final String EVENT_SYNC = EVENT_SERVICE_PREFIX + ".event.sync";
	public static final String EVENT_CREATE_COMMAND = EVENT_SERVICE_PREFIX + ".event.command.create";
	public static final String EVENT_UPDATE_COMMAND = EVENT_SERVICE_PREFIX + ".event.command.update";
	public static final String EVENT_DELETE_COMMAND = EVENT_SERVICE_PREFIX + ".event.command.delete";
}
