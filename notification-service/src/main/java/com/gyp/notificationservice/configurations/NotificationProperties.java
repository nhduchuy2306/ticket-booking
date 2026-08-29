package com.gyp.notificationservice.configurations;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "notification.polling")
@Getter
@Setter
public class NotificationProperties {

	/**
	 * Interval in milliseconds for polling newly created events.
	 */
	private long newEventIntervalMs = 300000; // 5 minutes

	/**
	 * Interval in milliseconds for polling tomorrow event reminders.
	 */
	private long tomorrowReminderIntervalMs = 3600000; // 1 hour
}
