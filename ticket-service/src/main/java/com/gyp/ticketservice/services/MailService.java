package com.gyp.ticketservice.services;

import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

public interface MailService {
	void sendEmail(String to, String subject, Map<String, Object> model, String templateName);

	void sendEmailWithAttachment(String to, String subject, Map<String, Object> model, String templateName,
			byte[] attachmentBytes, String fileName);

	void sendEmailWithMultipleAttachments(String to, String subject, Map<String, Object> model, String templateName,
			Map<String, Pair<byte[], String>> attachments);
}
