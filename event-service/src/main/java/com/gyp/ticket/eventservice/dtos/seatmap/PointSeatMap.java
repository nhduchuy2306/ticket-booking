package com.gyp.ticket.eventservice.dtos.seatmap;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PointSeatMap implements SeatMap, HasSection {
	private List<Point> points;
	private List<Section> sections;

	@Override
	public String getType() {
		return PointSeatMap.class.getTypeName();
	}
}
