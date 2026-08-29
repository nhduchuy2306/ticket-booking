package com.gyp.commonmail.services;

import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

public interface MailService {

	/**
	 * Send a plain-text email.
	 *
	 * @param to      recipient email address
	 * @param subject email subject
	 * @param text    email body text
	 */
	void sendEmail(String to, String subject, String text);

	/**
	 * Send an HTML email using a Thymeleaf template.
	 *
	 * @param to           recipient email address
	 * @param subject      email subject
	 * @param model        template model variables
	 * @param templateName Thymeleaf template name
	 */
	void sendEmail(String to, String subject, Map<String, Object> model, String templateName);

	/**
	 * Send an HTML email with a single attachment.
	 */
	void sendEmailWithAttachment(String to, String subject, Map<String, Object> model,
			String templateName, byte[] attachmentBytes, String fileName);

	/**
	 * Send an HTML email with multiple attachments.
	 * Key: fileName, Value: Pair of (fileBytes, contentType)
	 */
	void sendEmailWithMultipleAttachments(String to, String subject, Map<String, Object> model,
			String templateName, Map<String, Pair<byte[], String>> attachments);
}
