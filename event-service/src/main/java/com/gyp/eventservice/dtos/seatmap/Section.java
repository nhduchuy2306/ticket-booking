package com.gyp.eventservice.dtos.seatmap;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Presented as a vertical grouping of rows within a section, with shared styling and spacing.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Section extends BaseSeatMap implements Positionable, Dimensional {
	private SectionType type;
	private String ticketTypeId;
	private Position position;
	private Dimension dimensions;
	private String borderRadius;
	private Double rowSpacing;
	private int capacity = 0;
	private RowLabelPosition labelPosition;
	private List<Row> rows = new ArrayList<>();

	public int getCapacity() {
		if(SectionType.SEATED.equals(type)) {
			return 0;
		}
		return capacity;
	}
}
