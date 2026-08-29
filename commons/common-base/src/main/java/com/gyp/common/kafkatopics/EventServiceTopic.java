package com.gyp.common.kafkatopics;

public final class EventServiceTopic {
	private EventServiceTopic() {
	}

	public static final String EVENT_SERVICE_PREFIX = "event";

	// Event topics
	public static final String EVENT_SYNC = EVENT_SERVICE_PREFIX + ".event.sync";
	public static final String EVENT_CREATE_COMMAND = EVENT_SERVICE_PREFIX + ".event.command.create";
	public static final String EVENT_UPDATE_COMMAND = EVENT_SERVICE_PREFIX + ".event.command.update";
	public static final String EVENT_DELETE_COMMAND = EVENT_SERVICE_PREFIX + ".event.command.delete";
	public static final String EVENT_TICKET_GENERATED = EVENT_SERVICE_PREFIX + ".event.ticket.generated";

	// Ticket topics
	public static final String TICKET_TYPE_SYNC = EVENT_SERVICE_PREFIX + ".ticket-type.sync";
	public static final String TICKET_TYPE_CREATE_COMMAND = EVENT_SERVICE_PREFIX + ".ticket-type.command.create";
	public static final String TICKET_TYPE_UPDATE_COMMAND = EVENT_SERVICE_PREFIX + ".ticket-type.command.update";
	public static final String TICKET_TYPE_DELETE_COMMAND = EVENT_SERVICE_PREFIX + ".ticket-type.command.delete";
	public static final String GENERATE_SEAT_MAP_COMMAND = "generate-seat-map.command";
}
