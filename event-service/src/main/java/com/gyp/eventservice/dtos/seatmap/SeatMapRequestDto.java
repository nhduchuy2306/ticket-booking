package com.gyp.eventservice.dtos.seatmap;

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
public class SeatMapRequestDto {
	private String name;
	private String venueType;
	private SeatConfig seatConfig;
	private StageConfig stageConfig;
	private String organizationId;
}
