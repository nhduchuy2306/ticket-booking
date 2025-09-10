package com.gyp.ticketservice.services.impl;

import java.util.Locale;
import java.util.Map;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import com.gyp.ticketservice.services.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
	private final JavaMailSender mailSender;
	private final TemplateEngine templateEngine;

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
			addPDFAttachment(helper, attachmentBytes, fileName);
			mailSender.send(message);
		} catch(MessagingException e) {
			log.error("Failed to send email with attachment", e);
		}
	}

	@Override
	public void sendEmailWithMultipleAttachments(String to, String subject,
			Map<String, Object> model, String templateName, Map<String, Pair<byte[], String>> attachments) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = createMimeMessageHelper(message, to, subject, model, templateName);

			if(attachments != null && !attachments.isEmpty()) {
				for(Map.Entry<String, Pair<byte[], String>> entry : attachments.entrySet()) {
					String fileName = entry.getKey();
					byte[] fileBytes = entry.getValue().getLeft();
					String contentType = entry.getValue().getRight();

					addAttachment(helper, fileBytes, fileName, contentType);
				}
			}

			mailSender.send(message);
		} catch(MessagingException e) {
			log.error("Failed to send email with multiple attachments", e);
		}
	}

	private void addAttachment(MimeMessageHelper helper, byte[] attachmentBytes,
			String fileName, String contentType) throws MessagingException {
		if(attachmentBytes != null && attachmentBytes.length > 0) {
			try {
				ByteArrayResource resource = new ByteArrayResource(attachmentBytes) {
					@Override
					public String getFilename() {
						return fileName;
					}
				};
				helper.addAttachment(fileName, resource, contentType);
				log.debug("Attachment added successfully: {} (type: {})", fileName, contentType);
			} catch(MessagingException e) {
				log.error("Error adding attachment: {}", fileName, e);
				throw e;
			}
		}
	}

	private MimeMessageHelper createMimeMessageHelper(MimeMessage message, String to, String subject,
			Map<String, Object> model, String templateName) throws MessagingException {
		String content = templateEngine.process(templateName, new Context(Locale.getDefault(), model));

		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
		helper.setFrom("no-reply@gmail.com");
		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(content, true);
		return helper;
	}

	private void addPDFAttachment(MimeMessageHelper helper, byte[] attachmentBytes, String fileName)
			throws MessagingException {
		if(attachmentBytes != null && attachmentBytes.length > 0) {
			try {
				ByteArrayResource dataSource = new ByteArrayResource(attachmentBytes) {
					@Override
					public String getFilename() {
						return fileName;
					}
				};
				helper.addAttachment(fileName, dataSource, MediaType.APPLICATION_PDF_VALUE);
				log.debug("Attachment added successfully: {}", fileName);
			} catch(MessagingException e) {
				log.error("Error adding attachment: {}", fileName, e);
				throw e;
			}
		}
	}
}
