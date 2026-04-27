package com.gyp.notificationservice.configurations;

public final class Constants {
  private Constants() {
  }

  public static final int PORT = 9005;
  public static final String SEND_EMAIL_PATH = "/send-email";
  public static final String EMAIL_ADDRESS = "email.sender";

  public static final String EVENT_SERVICE_BASE_URL = "http://localhost:9001";
  public static final String TICKET_SERVICE_BASE_URL = "http://localhost:9002";
  public static final String AUTH_SERVICE_BASE_URL = "http://localhost:9000";
  public static final String CREATED_EVENTS_PATH = "/events/created-since";
  public static final String TOMORROW_EVENTS_PATH = "/events/tomorrow";
  public static final String TICKETS_BY_EVENT_PATH = "/tickets/by-event";
  public static final String CUSTOMERS_PATH = "/customer/auth/customers";
  public static final long CREATED_EVENT_POLL_INTERVAL_MS = 5 * 60 * 1000L;
  public static final long TOMORROW_REMINDER_POLL_INTERVAL_MS = 60 * 60 * 1000L;

  // Mail config
  public static final String SMTP_HOST = "smtp.example.com";
  public static final int SMTP_PORT = 587;
  public static final String SMTP_USERNAME = "your@email.com";
  public static final String SMTP_PASSWORD = "yourpassword";
}
