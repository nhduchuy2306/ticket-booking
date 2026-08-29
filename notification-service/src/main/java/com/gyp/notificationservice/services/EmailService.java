package com.gyp.notificationservice.services;

public interface EmailService {

	/**
	 * Send a plain-text email.
	 *
	 * @param to      recipient email address
	 * @param subject email subject
	 * @param text    email body text
	 */
	void sendEmail(String to, String subject, String text);
}
