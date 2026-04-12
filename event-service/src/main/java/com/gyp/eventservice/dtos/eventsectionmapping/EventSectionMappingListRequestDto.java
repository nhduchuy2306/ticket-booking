package com.gyp.eventservice.dtos.eventsectionmapping;

import java.util.List;

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
public class EventSectionMappingListRequestDto {
	private List<EventSectionMappingRequestDto> eventSectionMappingRequestDtos;
}
