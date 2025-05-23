package com.gyp.eventservice.dtos.seatmap2;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Lớp đại diện cho một khu vực trong venue
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Section extends BaseEntity implements Positionable, Dimensional, Rotatable, ArcLayout {
	private SectionType type;
	private Position position;
	private Dimension dimensions;
	private double rotation;
	private int capacity;
	private String priceCategoryId;
	private List<Row> rows;
	private List<Table> tables;
	private boolean isArc;
	private ArcProperties arcProperties;

	@Override
	public void setArcProperties(ArcProperties arcProperties) {
		this.arcProperties = arcProperties;
		isArc = (arcProperties != null);
	}

	public void addRow(Row row) {
		rows.add(row);
	}

	public void addTable(Table table) {
		tables.add(table);
	}
}
