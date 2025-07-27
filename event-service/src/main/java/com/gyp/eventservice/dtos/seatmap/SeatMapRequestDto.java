package com.gyp.eventservice.dtos.seatmap;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
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
