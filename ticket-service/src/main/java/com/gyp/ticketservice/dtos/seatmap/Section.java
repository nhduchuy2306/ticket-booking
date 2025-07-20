package com.gyp.ticketservice.dtos.seatmap;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Class for one section in venue
 */
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Section extends BaseSeatMap implements Positionable, Dimensional, Rotatable, ArcLayout {
	private SectionType type;
	private Position position;
	private Dimension dimensions;
	private double rotation;
	private int capacity;
	private List<Row> rows = new ArrayList<>();
	private List<Table> tables = new ArrayList<>();
	private Boolean isArc;
	private ArcProperties arcProperties;
	private String ticketTypeId;

	@Override
	public void setArcProperties(ArcProperties arcProperties) {
		this.arcProperties = arcProperties;
		setIsArc(arcProperties != null);
	}

	public void addRow(Row row) {
		rows.add(row);
	}

	public void addTable(Table table) {
		tables.add(table);
	}

	@Override
	public Boolean getIsArc() {
		return isArc != null ? isArc : false;
	}

	@Override
	public void setIsArc(Boolean isArc) {
		this.isArc = isArc;
	}
}
