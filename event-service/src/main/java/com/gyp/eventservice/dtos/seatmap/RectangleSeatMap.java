package com.gyp.eventservice.dtos.seatmap;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RectangleSeatMap implements SeatMap, HasSection {
	private Integer totalRows;
	private Integer seatsPerRow;
	private List<Section> sections;

	@Override
	public String getType() {
		return RectangleSeatMap.class.getTypeName();
	}
}
