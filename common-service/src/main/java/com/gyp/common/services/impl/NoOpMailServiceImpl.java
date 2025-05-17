package com.gyp.common.services.impl;

import java.util.Map;

import com.gyp.common.services.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@ConditionalOnMissingBean(JavaMailSender.class)
public class NoOpMailServiceImpl implements MailService {
	@Override
	public void sendEmail(String to, String subject, Map<String, Object> model, String templateName) {
		log.debug("Email sending skipped (no JavaMailSender available): to={}, subject={}, template={}",
				to, subject, templateName);
	}

	@Override
	public void sendEmailWithAttachment(String to, String subject, Map<String, Object> model, String templateName,
			byte[] attachmentBytes, String fileName) {
		log.debug(
				"Email with attachment sending skipped (no JavaMailSender available): to={}, subject={}, template={}, fileName={}",
				to, subject, templateName, fileName);
	}
}
