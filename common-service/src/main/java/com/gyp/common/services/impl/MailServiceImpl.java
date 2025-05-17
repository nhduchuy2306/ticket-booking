package com.gyp.common.services.impl;

import java.util.Locale;
import java.util.Map;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import com.gyp.common.services.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnBean({ JavaMailSender.class, TemplateEngine.class })
public class MailServiceImpl implements MailService {
	private final JavaMailSender mailSender;
	private final TemplateEngine templateEngine;

	private static final String DEFAULT_FROM = "no-reply@gmail.com";

	@Override
	public void sendEmail(String to, String subject, Map<String, Object> model, String templateName) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			createMimeMessageHelper(message, to, subject, model, templateName);
			mailSender.send(message);
		} catch(MessagingException e) {
			log.error("Failed to send email", e);
		}
	}

	@Override
	public void sendEmailWithAttachment(String to, String subject, Map<String, Object> model,
			String templateName, byte[] attachmentBytes, String fileName) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = createMimeMessageHelper(message, to, subject, model, templateName);
			addAttachment(helper, attachmentBytes, fileName);
			mailSender.send(message);
		} catch(MessagingException e) {
			log.error("Failed to send email with attachment", e);
		}
	}

	private MimeMessageHelper createMimeMessageHelper(MimeMessage message, String to, String subject,
			Map<String, Object> model, String templateName) throws MessagingException {
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
		helper.setFrom(DEFAULT_FROM);
		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(generateEmailContent(model, templateName), true);
		return helper;
	}

	private String generateEmailContent(Map<String, Object> model, String templateName) {
		return templateEngine.process(templateName, new Context(Locale.getDefault(), model));
	}

	private void addAttachment(MimeMessageHelper helper, byte[] attachmentBytes, String fileName)
			throws MessagingException {
		if(attachmentBytes != null && attachmentBytes.length > 0) {
			try {
				ByteArrayResource dataSource = new ByteArrayResource(attachmentBytes) {
					@Override
					public String getFilename() {
						return fileName;
					}
				};
				helper.addAttachment(fileName, dataSource, "application/pdf");
				log.debug("Attachment added successfully: {}", fileName);
			} catch(MessagingException e) {
				log.error("Error adding attachment: {}", fileName, e);
				throw e;
			}
		}
	}
}
