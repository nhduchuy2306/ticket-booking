package com.gyp.common.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeatMapEventModel {
	private String name;
	private String venueType;
	private String seatConfig;
	private String stageConfig;
}
