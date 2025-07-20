package com.gyp.eventservice.dtos.seatmap;

import com.gyp.eventservice.dtos.AbstractDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SeatMapResponseDto extends AbstractDto {
	private String id;
	private String name;
	private String venueType;
	private SeatConfig seatConfig;
	private StageConfig stageConfig;
}
