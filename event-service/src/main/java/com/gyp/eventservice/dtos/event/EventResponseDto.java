package com.gyp.eventservice.dtos.event;

import java.time.LocalDateTime;
import java.util.List;

import com.gyp.common.enums.event.EventStatus;
import com.gyp.eventservice.dtos.AbstractDto;
import com.gyp.eventservice.dtos.category.CategoryResponseDto;
import com.gyp.eventservice.dtos.eventapproval.EventApprovalResponseDto;
import com.gyp.eventservice.dtos.eventpromotion.EventPromotionResponseDto;
import com.gyp.eventservice.dtos.organizer.OrganizerResponseDto;
import com.gyp.eventservice.dtos.season.SeasonResponseDto;
import com.gyp.eventservice.dtos.tickettype.TicketTypeResponseDto;
import com.gyp.eventservice.dtos.venue.VenueResponseDto;
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
public class EventResponseDto extends AbstractDto {
	private String id;
	private String name;
	private String description;
	private EventStatus status;

	// Time-related properties
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private LocalDateTime doorOpenTime;
	private LocalDateTime doorCloseTime;

	// Related entities - more detailed information
	private OrganizerResponseDto organizer;
	private VenueResponseDto venue;
	private SeasonResponseDto season;
	private List<CategoryResponseDto> categories;

	// Associated collections - depending on use case, these might be included
	private List<TicketTypeResponseDto> ticketTypes;
	private List<EventPromotionResponseDto> promotions;
	private List<EventApprovalResponseDto> approvals;

	// Additional useful information for the UI
	private long ticketsSold;
	private boolean isEventInProgress;
	private boolean isEventCompleted;
	private String organizationId;
}
