package com.gyp.common.kafkatopics;

public final class TopicConstants {
	private TopicConstants() {
	}

	// Auth Service Topics
	private static final String AUTH_SERVICE_PREFIX = "auth";
	public static final String USER_ACCOUNT_SYNC = AUTH_SERVICE_PREFIX + ".user.account.sync";
	public static final String ORGANIZER_CREATE_COMMAND = AUTH_SERVICE_PREFIX + ".organizer.command.create";
	public static final String ORGANIZER_UPDATE_COMMAND = AUTH_SERVICE_PREFIX + ".organizer.command.create";
	public static final String ORGANIZER_DELETE_COMMAND = AUTH_SERVICE_PREFIX + ".organizer.command.create";

	// Event topics
	public static final String EVENT_SERVICE_PREFIX = "event";
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
	public static final String SEAT_MAP_SYNC = EVENT_SERVICE_PREFIX + ".seat-map.sync";

}
