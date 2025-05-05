package com.gyp.ticket.eventservice.dtos.event;

import java.time.LocalDateTime;

import com.gyp.common.enums.event.EventStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventSummaryDto {
	private String id;
	private String name;
	private EventStatus status;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
}
