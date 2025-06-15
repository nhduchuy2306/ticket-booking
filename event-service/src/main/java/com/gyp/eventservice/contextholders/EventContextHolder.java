package com.gyp.eventservice.contextholders;

import java.util.Optional;

import jakarta.servlet.http.HttpSession;

import com.gyp.eventservice.entities.EventEntity;
import com.gyp.eventservice.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventContextHolder {
	private final EventRepository eventRepository;
	private final HttpSession session;

	public void setCurrentEventId(String eventId) {
		session.setAttribute("eventId", eventId);
	}

	public Optional<String> getCurrentEventId() {
		var eventId = session.getAttribute("eventId");
		if(eventId == null) {
			return Optional.empty();
		}
		return Optional.of((String)eventId);
	}

	public EventEntity getCurrentEvent() {
		Optional<String> eventId = getCurrentEventId();
		if(eventId.isEmpty()) {
			return null;
		}
		return eventRepository.findById(String.valueOf(eventId))
				.orElseThrow(() -> new RuntimeException("Event not found with id: " + eventId));
	}
}
