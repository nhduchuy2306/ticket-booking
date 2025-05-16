package com.gyp.common.services;

import java.util.Map;

public interface MailService {
	void sendEmail(String to, String subject, Map<String, Object> model, String templateName);

	void sendEmailWithAttachment(String to, String subject, Map<String, Object> model, String templateName,
			byte[] attachmentBytes, String fileName);
}
