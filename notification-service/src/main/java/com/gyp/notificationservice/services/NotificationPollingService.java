package com.gyp.notificationservice.services;

import com.gyp.notificationservice.configurations.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationPollingService {

	private final EmailService emailService;
	private final RestTemplate restTemplate;

	private final Set<String> sentNewEventKeys = ConcurrentHashMap.newKeySet();
	private final Set<String> sentReminderKeys = ConcurrentHashMap.newKeySet();
	private volatile LocalDateTime lastCreatedEventCheck = LocalDateTime.now().minusMinutes(10);

	/**
	 * Poll for newly created events and notify all customers.
	 * Interval is configurable via notification.polling.new-event-interval-ms (default: 5 min).
	 */
	@Scheduled(fixedDelayString = "${notification.polling.new-event-interval-ms:300000}")
	public void pollNewEvents() {
		try {
			String url = UriComponentsBuilder
					.fromHttpUrl(Constants.EVENT_SERVICE_BASE_URL + Constants.CREATED_EVENTS_PATH)
					.queryParam("since", lastCreatedEventCheck.toString())
					.toUriString();

			List<Map<String, Object>> events = fetchJsonArray(url);

			if (events == null || events.isEmpty()) {
				return;
			}

			updateCreatedEventCheckpoint(events);

			List<String> customerEmails = fetchCustomerEmails();
			if (customerEmails.isEmpty()) {
				return;
			}

			List<Map<String, Object>> pendingEvents = events.stream()
					.filter(event -> sentNewEventKeys.add(buildEventKey(event)))
					.toList();

			if (pendingEvents.isEmpty()) {
				return;
			}

			for (Map<String, Object> event : pendingEvents) {
				for (String customerEmail : customerEmails) {
					String eventName = getStringValue(event, "name", "an upcoming event");
					emailService.sendEmail(
							customerEmail,
							"New event coming soon: " + eventName,
							buildNewEventBody(event)
					);
				}
			}
		} catch (Exception e) {
			log.error("Failed to poll newly created events", e);
		}
	}

	/**
	 * Poll for events happening tomorrow and send reminders to ticket holders.
	 * Interval is configurable via notification.polling.tomorrow-reminder-interval-ms (default: 1 hour).
	 */
	@Scheduled(fixedDelayString = "${notification.polling.tomorrow-reminder-interval-ms:3600000}")
	public void pollTomorrowReminders() {
		try {
			String url = Constants.EVENT_SERVICE_BASE_URL + Constants.TOMORROW_EVENTS_PATH;
			List<Map<String, Object>> events = fetchJsonArray(url);

			if (events == null || events.isEmpty()) {
				return;
			}

			for (Map<String, Object> event : events) {
				if (!sentReminderKeys.add(buildReminderKey(event))) {
					continue;
				}

				String eventId = getStringValue(event, "id", null);
				if (eventId == null || eventId.isBlank()) {
					continue;
				}

				String ticketsUrl = Constants.TICKET_SERVICE_BASE_URL + Constants.TICKETS_BY_EVENT_PATH + "/" + eventId;
				List<Map<String, Object>> tickets = fetchJsonArray(ticketsUrl);

				if (tickets == null || tickets.isEmpty()) {
					continue;
				}

				Set<String> recipients = new HashSet<>();
				for (Map<String, Object> ticket : tickets) {
					String email = getStringValue(ticket, "attendeeEmail", null);
					if (email != null && !email.isBlank()) {
						recipients.add(email);
					}
				}

				String eventName = getStringValue(event, "name", "your event");
				for (String recipient : recipients) {
					emailService.sendEmail(
							recipient,
							"Reminder: " + eventName + " is tomorrow",
							buildTomorrowBody(event)
					);
				}
			}
		} catch (Exception e) {
			log.error("Failed to poll tomorrow events", e);
		}
	}

	private List<String> fetchCustomerEmails() {
		try {
			String url = Constants.AUTH_SERVICE_BASE_URL + Constants.CUSTOMERS_PATH;
			List<Map<String, Object>> customers = fetchJsonArray(url);

			if (customers == null || customers.isEmpty()) {
				return Collections.emptyList();
			}

			return customers.stream()
					.map(customer -> getStringValue(customer, "email", null))
					.filter(email -> email != null && !email.isBlank())
					.toList();
		} catch (Exception e) {
			log.error("Failed to fetch customers for new event notifications", e);
			return Collections.emptyList();
		}
	}

	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> fetchJsonArray(String url) {
		try {
			ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
					url,
					HttpMethod.GET,
					null,
					new ParameterizedTypeReference<>() {
					}
			);
			return response.getBody();
		} catch (Exception e) {
			log.error("Failed to fetch data from: {}", url, e);
			return Collections.emptyList();
		}
	}

	private String buildNewEventBody(Map<String, Object> event) {
		String note = getStringValue(event, "note", null);
		String startTime = getStringValue(event, "startTime", null);
		return "A new event has just been created: " + getStringValue(event, "name", "Unknown event") +
				(startTime != null ? "\nStart time: " + startTime : "") +
				(note != null && !note.isBlank() ? "\nNote: " + note : "");
	}

	private String buildTomorrowBody(Map<String, Object> event) {
		String note = getStringValue(event, "note", null);
		String startTime = getStringValue(event, "startTime", null);
		return "Your event is happening tomorrow: " + getStringValue(event, "name", "Unknown event") +
				(startTime != null ? "\nStart time: " + startTime : "") +
				(note != null && !note.isBlank() ? "\nReminder notes: " + note : "");
	}

	private String buildEventKey(Map<String, Object> event) {
		return getStringValue(event, "id", "") + ":" + getStringValue(event, "createTimestamp", "");
	}

	private String buildReminderKey(Map<String, Object> event) {
		return getStringValue(event, "id", "") + ":" + getStringValue(event, "startTime", "");
	}

	private void updateCreatedEventCheckpoint(List<Map<String, Object>> events) {
		LocalDateTime maxCreateTimestamp = events.stream()
				.map(item -> getStringValue(item, "createTimestamp", null))
				.filter(value -> value != null && !value.isBlank())
				.map(LocalDateTime::parse)
				.max(LocalDateTime::compareTo)
				.orElse(lastCreatedEventCheck);

		if (maxCreateTimestamp.isAfter(lastCreatedEventCheck)) {
			lastCreatedEventCheck = maxCreateTimestamp;
		}
	}

	private String getStringValue(Map<String, Object> map, String key, String defaultValue) {
		Object value = map.get(key);
		return value != null ? value.toString() : defaultValue;
	}
}
