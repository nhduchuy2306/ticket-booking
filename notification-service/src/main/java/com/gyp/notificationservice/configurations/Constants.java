package com.gyp.notificationservice.configurations;

public final class Constants {
	private Constants() {
	}

	public static final String SEND_EMAIL_PATH = "/send-email";

	public static final String EVENT_SERVICE_BASE_URL = "http://localhost:9001";
	public static final String TICKET_SERVICE_BASE_URL = "http://localhost:9002";
	public static final String AUTH_SERVICE_BASE_URL = "http://localhost:9000";
	public static final String CREATED_EVENTS_PATH = "/events/created-since";
	public static final String TOMORROW_EVENTS_PATH = "/events/tomorrow";
	public static final String TICKETS_BY_EVENT_PATH = "/tickets/by-event";
	public static final String CUSTOMERS_PATH = "/customer/auth/customers";
}
