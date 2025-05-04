package com.gyp.ticket.eventservice.dtos.seatmap;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

// For custom stage (festival, open area)
@Data
@NoArgsConstructor
public class CustomSeatMap implements SeatMap {
	private List<Zone> zones;
	private String svgPath;

	@Override
	public String getType() {
		return CustomSeatMap.class.getTypeName();
	}
}
