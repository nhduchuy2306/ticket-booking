package com.gyp.notificationservice.configurations;

public final class Constants {
  private Constants() {
  }

  public static final int PORT = 9005;
  public static final String SEND_EMAIL_PATH = "/send-email";
  public static final String EMAIL_ADDRESS = "email.sender";

  // Mail config
  public static final String SMTP_HOST = "smtp.example.com";
  public static final int SMTP_PORT = 587;
  public static final String SMTP_USERNAME = "your@email.com";
  public static final String SMTP_PASSWORD = "yourpassword";
}
