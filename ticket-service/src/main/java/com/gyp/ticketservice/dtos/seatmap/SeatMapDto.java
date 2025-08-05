package com.gyp.ticketservice.dtos.seatmap;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SeatMapDto {
	private String id;
	private String name;
	private String eventName;
	private String eventDateTime;
	private String venueType;
	private String organizationId;
	private SeatConfig seatConfig;
	private StageConfig stageConfig;
}
