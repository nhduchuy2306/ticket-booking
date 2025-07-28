package com.gyp.eventservice.dtos.event;

import java.time.LocalDateTime;
import java.util.List;

import com.gyp.common.enums.event.EventStatus;
import com.gyp.eventservice.dtos.AbstractDto;
import com.gyp.eventservice.dtos.category.CategoryDto;
import com.gyp.eventservice.dtos.organizer.OrganizerDto;
import com.gyp.eventservice.dtos.tickettype.TicketTypeDto;
import com.gyp.eventservice.dtos.venuemap.VenueMapDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EventDto extends AbstractDto {
	private String id;
	private String name;
	private String description;
	private EventStatus status;
	private String organizationId;

	// Time-related properties
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private LocalDateTime doorOpenTime;
	private LocalDateTime doorCloseTime;

	// Related entities - more detailed information
	private OrganizerDto organizer;
	private VenueMapDto venueMap;
	private List<CategoryDto> categories;
	private List<TicketTypeDto> ticketTypes;
}
