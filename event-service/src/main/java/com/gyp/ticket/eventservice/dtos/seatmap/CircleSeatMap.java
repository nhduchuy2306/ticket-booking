package com.gyp.ticket.eventservice.dtos.seatmap;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CircleSeatMap implements SeatMap, HasSection {
	private Integer centerX;
	private Integer centerY;
	private Integer radius;
	private Double angleStep; // Angle between seat (360°/seatCount)
	private List<Section> sections;

	@Override
	public String getType() {
		return CircleSeatMap.class.getTypeName();
	}
}
