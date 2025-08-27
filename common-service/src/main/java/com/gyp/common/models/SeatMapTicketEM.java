package com.gyp.common.models;

import java.time.LocalDateTime;

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
public class SeatMapTicketEM {
	private String id;
	private String name;
	private String eventId;
	private String eventName;
	private LocalDateTime eventDateTime;
	private String venueType;
	private String organizationId;
	private String seatConfig;
	private String stageConfig;
}
