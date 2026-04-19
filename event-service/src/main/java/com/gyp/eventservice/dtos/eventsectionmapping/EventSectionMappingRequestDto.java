package com.gyp.eventservice.dtos.eventsectionmapping;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventSectionMappingRequestDto {
	private String eventId;
	private String ticketTypeId;
	private String seatMapId;
	private String sectionId;
}
