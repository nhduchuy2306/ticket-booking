package com.gyp.common.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SeatMapEventModel {
	private String name;
	private String venueType;
	private String seatConfig;
	private String stageConfig;
}
