package com.gyp.common.kafkatopics;

public final class TopicConstants {
	private TopicConstants() {
	}

	// Auth Service Topics
	public static final String USER_ACCOUNT_SYNC = "auth.user.account.sync";
	public static final String ORGANIZER_CREATE_COMMAND = "auth.organizer.command.create";
	public static final String ORGANIZER_UPDATE_COMMAND = "auth.organizer.command.create";
	public static final String ORGANIZER_DELETE_COMMAND = "auth.organizer.command.create";

	// Event topics
	public static final String EVENT_CREATE_COMMAND = "event.command.create";
	public static final String EVENT_UPDATE_COMMAND = "event.command.update";
	public static final String EVENT_DELETE_COMMAND = "event.command.delete";
	public static final String EVENT_TICKET_GENERATED = "event.ticket.generated";

	// Ticket topics
	public static final String TICKET_TYPE_CREATE_COMMAND = "event.ticket-type.command.create";
	public static final String TICKET_TYPE_UPDATE_COMMAND = "event.ticket-type.command.update";
	public static final String TICKET_TYPE_DELETE_COMMAND = "event.ticket-type.command.delete";
	public static final String SEAT_MAP_SYNC = "event.seat-map.sync";

	public static final String ASSIGN_SALE_CHANNEL_TO_EVENT = "event.assign-sale-channel-to-event";
	public static final String EVENT_ON_SALE_EVENT = "event.on.sale.event";
	public static final String ORDER_CREATED_EVENT = "order.created.event";
	public static final String GENERATE_TICKET_PDF_EVENT = "generate.ticket.pdf.event";
	public static final String SEND_EMAIL_EVENT = "send-email.event";
}
