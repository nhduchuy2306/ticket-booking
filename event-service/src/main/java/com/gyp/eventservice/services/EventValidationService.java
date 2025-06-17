package com.gyp.eventservice.services;

import java.util.List;

import com.gyp.common.dtos.validation.ValidationResult;
import com.gyp.eventservice.dtos.event.EventRequestDto;
import com.gyp.eventservice.dtos.tickettype.TicketTypeRequestDto;
import com.gyp.eventservice.entities.EventTimeEmbeddable;

public interface EventValidationService {
	ValidationResult validateEventCreation(EventRequestDto request);

	ValidationResult validateEventPublication(String eventId);

	ValidationResult validateTicketTypeConfiguration(List<TicketTypeRequestDto> ticketTypes);

	ValidationResult validateEventTiming(EventTimeEmbeddable timing);
}
