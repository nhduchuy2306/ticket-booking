package com.gyp.eventservice.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import com.gyp.common.dtos.validation.ValidationResult;
import com.gyp.common.enums.event.EventStatus;
import com.gyp.common.exceptions.ResourceNotFoundException;
import com.gyp.eventservice.dtos.event.EventRequestDto;
import com.gyp.eventservice.dtos.event.EventResponseDto;
import com.gyp.eventservice.dtos.tickettype.TicketTypeRequestDto;
import com.gyp.eventservice.entities.EventTimeEmbeddable;
import com.gyp.eventservice.exceptions.EventNotFoundException;
import com.gyp.eventservice.exceptions.OrganizerNotFoundException;
import com.gyp.eventservice.exceptions.VenueNotFoundException;
import com.gyp.eventservice.services.CategoryService;
import com.gyp.eventservice.services.EventService;
import com.gyp.eventservice.services.EventValidationService;
import com.gyp.eventservice.services.OrganizerService;
import com.gyp.eventservice.services.VenueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventValidationServiceImpl implements EventValidationService {
	private final VenueService venueService;
	private final OrganizerService organizerService;
	private final CategoryService categoryService;
	private final EventService eventService;

	@Override
	public ValidationResult validateEventCreation(EventRequestDto request) {
		ValidationResult result = ValidationResult.success();

		// Basic field validation
		if(request.getName() == null || request.getName().trim().isEmpty()) {
			result.addError("name", "Event name is required");
		} else if(request.getName().length() > 255) {
			result.addError("name", "Event name cannot exceed 255 characters");
		}

		if(request.getDescription() == null || request.getDescription().trim().isEmpty()) {
			result.addError("description", "Event description is required");
		}

		// Organizer validation
		if(request.getOrganizerId() == null) {
			result.addError("organizerId", "Organizer is required");
		} else {
			try {
				organizerService.getOrganizerById(request.getOrganizerId());
			} catch(OrganizerNotFoundException e) {
				result.addError("organizerId", "Organizer not found");
			}
		}

		// Venue validation
		if(request.getVenueId() == null) {
			result.addError("venueId", "Venue is required");
		} else {
			try {
				venueService.getVenueById(request.getVenueId());
			} catch(VenueNotFoundException e) {
				result.addError("venueId", "Venue not found");
			}
		}

		// Time validation
		ValidationResult timeValidation = validateEventTiming(
				EventTimeEmbeddable.builder()
						.startTime(request.getStartTime())
						.endTime(request.getEndTime())
						.doorOpenTime(request.getDoorOpenTime())
						.doorCloseTime(request.getDoorCloseTime())
						.build());
		result = result.combine(timeValidation);

		// Category validation
		if(request.getCategoryIds() != null && !request.getCategoryIds().isEmpty()) {
			ValidationResult categoryValidation = validateCategories(request.getCategoryIds());
			result = result.combine(categoryValidation);
		}

		return result;
	}

	@Override
	public ValidationResult validateEventPublication(String eventId) {
		ValidationResult result = ValidationResult.success();

		try {
			EventResponseDto event = eventService.getEventById(eventId);

			// Check if event has ticket types
			//			if(event.getTicketTypeEntityList() == null || event.getTicketTypeEntityList().isEmpty()) {
			//				result.addError("ticketTypes", "Cannot publish event without ticket types");
			//			}

			// Check if event is already published
			if(Objects.equals(event.getStatus(), EventStatus.PUBLISHED)) {
				result.addError("status", "Event is already published");
			}
			//
			//			// Check if event is approved (if approval is required)
			//			boolean isApproved = event.getEventApprovalEntityList().stream()
			//					.anyMatch(approval -> approval.getStatus() == ApprovalStatus.APPROVED);
			//
			//			if(!isApproved) {
			//				result.addError("approval", "Event must be approved before publication");
			//			}

			// Check if venue is still available
			// This could involve checking with a booking system

		} catch(EventNotFoundException e) {
			result.addError("eventId", "Event not found");
		}

		return result;
	}

	@Override
	public ValidationResult validateTicketTypeConfiguration(List<TicketTypeRequestDto> ticketTypes) {
		ValidationResult result = ValidationResult.success();

		if(ticketTypes == null || ticketTypes.isEmpty()) {
			result.addError("ticketTypes", "At least one ticket type is required");
			return result;
		}

		for(int i = 0; i < ticketTypes.size(); i++) {
			TicketTypeRequestDto ticketType = ticketTypes.get(i);
			String fieldPrefix = "ticketTypes[" + i + "]";

			// Name validation
			if(ticketType.getName() == null || ticketType.getName().trim().isEmpty()) {
				result.addError(fieldPrefix + ".name", "Ticket type name is required");
			}

			// Price validation
			if(ticketType.getPrice() < 0) {
				result.addError(fieldPrefix + ".price", "Ticket price cannot be negative");
			}

			// Quantity validation
			if(ticketType.getQuantityAvailable() != null && ticketType.getQuantityAvailable() < 0) {
				result.addError(fieldPrefix + ".quantityAvailable", "Available quantity cannot be negative");
			}

			// Sale date validation
			if(ticketType.getSaleStartDate() != null && ticketType.getSaleEndDate() != null) {
				if(ticketType.getSaleEndDate().isBefore(ticketType.getSaleStartDate())) {
					result.addError(fieldPrefix + ".saleEndDate", "Sale end date must be after sale start date");
				}
			}

			// Check for duplicate names
			for(int j = i + 1; j < ticketTypes.size(); j++) {
				if(ticketTypes.get(j).getName().equals(ticketType.getName())) {
					result.addError(fieldPrefix + ".name", "Duplicate ticket type name: " + ticketType.getName());
				}
			}
		}

		return result;
	}

	@Override
	public ValidationResult validateEventTiming(EventTimeEmbeddable timing) {
		ValidationResult result = ValidationResult.success();

		if(timing == null) {
			result.addError("time", "Event timing is required");
			return result;
		}

		LocalDateTime now = LocalDateTime.now();

		// Start time validation
		if(timing.getStartTime() == null) {
			result.addError("time.startTime", "Event start time is required");
		} else if(timing.getStartTime().isBefore(now)) {
			result.addError("time.startTime", "Event start time cannot be in the past");
		}

		// End time validation
		if(timing.getEndTime() == null) {
			result.addError("time.endTime", "Event end time is required");
		} else if(timing.getStartTime() != null &&
				timing.getEndTime().isBefore(timing.getStartTime())) {
			result.addError("time.endTime", "Event end time must be after start time");
		}

		// Door time validation
		if(timing.getDoorOpenTime() != null) {
			if(timing.getStartTime() != null &&
					timing.getDoorOpenTime().isAfter(timing.getStartTime())) {
				result.addError("time.doorOpenTime", "Door open time must be before event start time");
			}

			if(timing.getDoorOpenTime().isBefore(now)) {
				result.addWarning("time.doorOpenTime", "Door open time is in the past");
			}
		}

		if(timing.getDoorCloseTime() != null) {
			if(timing.getEndTime() != null &&
					timing.getDoorCloseTime().isBefore(timing.getEndTime())) {
				result.addWarning("time.doorCloseTime", "Door close time is before event end time");
			}
		}

		return result;
	}

	private ValidationResult validateCategories(List<String> categoryIds) {
		ValidationResult result = ValidationResult.success();

		for(String categoryId : categoryIds) {
			try {
				categoryService.getCategoryById(categoryId);
			} catch(ResourceNotFoundException e) {
				result.addError("categoryIds", "Category not found: " + categoryId);
			}
		}

		return result;
	}
}
