package com.gyp.eventservice.dtos.seatmap;

import com.gyp.eventservice.dtos.AbstractDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Persisted seat map layout returned to the editor and viewer.
 * It contains the serialized seat/stage layout, but not per-event runtime seat
 * inventory, which is handled by SeatInventoryEntity and SeatHoldEntity.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatMapResponseDto extends AbstractDto {
	private String id;
	private String name;
	private String venueType;
	private SeatConfig seatConfig;
	private StageConfig stageConfig;
	private String organizationId;
}
