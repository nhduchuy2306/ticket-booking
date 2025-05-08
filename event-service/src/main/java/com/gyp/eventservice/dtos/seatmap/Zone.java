package com.gyp.eventservice.dtos.seatmap;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Zone {
	private String name;
	private String seatType;
	private int zoneX, zoneY, width, height;
	private SeatMap area; // This is use for composite
}
