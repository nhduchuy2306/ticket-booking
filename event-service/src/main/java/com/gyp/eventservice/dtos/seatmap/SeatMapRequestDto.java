package com.gyp.eventservice.dtos.seatmap;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Input payload for creating or updating a seat map.
 * The layout stays as DTO data so editor-side changes do not couple to seat
 * inventory tables.
 */
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
