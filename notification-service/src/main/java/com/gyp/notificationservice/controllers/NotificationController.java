package com.gyp.notificationservice.controllers;

import com.gyp.commonmail.services.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class NotificationController {

	private final MailService mailService;

	@PostMapping("/send-email")
	public ResponseEntity<String> sendEmail(@RequestBody Map<String, String> payload) {
		String to = payload.get("to");

		if (to == null || to.isBlank()) {
			return ResponseEntity.badRequest().body("Missing recipient email");
		}

		String subject = payload.getOrDefault("subject", "Notification");
		String text = payload.getOrDefault("text", "This is a notification email.");

		try {
			mailService.sendEmail(to, subject, text);
			return ResponseEntity.ok("Email sent successfully");
		} catch (Exception e) {
			log.error("Failed to send email", e);
			return ResponseEntity.internalServerError().body("Failed to send email: " + e.getMessage());
		}
	}
}
