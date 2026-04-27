package com.gyp.eventservice.dtos.event;

import java.time.LocalDateTime;
import java.util.List;

import com.gyp.common.enums.event.EventStatus;
import com.gyp.eventservice.dtos.AbstractDto;
import com.gyp.eventservice.dtos.category.CategoryResponseDto;
import com.gyp.eventservice.dtos.eventapproval.EventApprovalResponseDto;
import com.gyp.eventservice.dtos.eventpromotion.EventPromotionResponseDto;
import com.gyp.eventservice.dtos.season.SeasonResponseDto;
import com.gyp.eventservice.dtos.tickettype.TicketTypeResponseDto;
import com.gyp.eventservice.dtos.venuemap.VenueMapResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventResponseDto extends AbstractDto {
	private String id;
	private String name;
	private String description;
	private String note;
	private EventStatus status;
	private Boolean isGenerated;
	private String logoUrl;
	private byte[] logoBufferArray;

	// Time-related properties
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private LocalDateTime doorOpenTime;
	private LocalDateTime doorCloseTime;

	// Related entities - more detailed information
	private VenueMapResponseDto venueMap;
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
