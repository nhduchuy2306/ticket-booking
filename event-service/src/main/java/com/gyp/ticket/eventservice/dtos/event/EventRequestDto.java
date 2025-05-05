package com.gyp.ticket.eventservice.dtos.event;

import java.time.LocalDateTime;
import java.util.List;

import com.gyp.common.enums.event.EventStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestDto {
	private String name;
	private String description;
	private EventStatus status;

	// Time-related properties from EventTimeEmbeddable
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private LocalDateTime doorOpenTime;
	private LocalDateTime doorCloseTime;

	// Related entities - using IDs instead of full entities
	private String organizerId;
	private String venueId;
	private List<String> categoryIds;
}
