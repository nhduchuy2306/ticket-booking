package com.gyp.ticket.eventservice.dtos.event;

import com.gyp.common.enums.event.EventStatus;
import com.gyp.ticket.eventservice.entities.EventTimeEmbeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventResponseDto {
	private String id;
	private String name;
	private String description;
	private EventStatus status;
	private EventTimeEmbeddable time;
	private String organizerId;
	private String venueId;
}
