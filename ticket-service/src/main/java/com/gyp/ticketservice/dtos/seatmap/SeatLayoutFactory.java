package com.gyp.ticketservice.dtos.seatmap;

import java.util.Objects;

public final class SeatLayoutFactory {
	private SeatLayoutFactory() {
	}

	/**
	 * Tạo một section với hàng thẳng
	 */
	public static Section createLinearSection(String name, Position position, int rows, int seatsPerRow,
			double rowSpacing, double seatSpacing) {
		Section section = new Section();
		section.setName(name);
		section.setPosition(position);
		section.setType(SectionType.SEATED);

		double currentY = 0;

		for(int i = 0; i < rows; i++) {
			Row row = new Row();
			row.setName(generateRowName(i));
			row.setPosition(new Position(0, currentY));

			row.generateLinearSeats(seatsPerRow, 0, seatSpacing);
			section.addRow(row);

			currentY += rowSpacing;
		}
		return section;
	}

	/**
	 * Tạo một section với hàng vòng cung
	 */
	public static Section createArcSection(String name, Position position, int rows, int seatsPerRow,
			double startRadius, double radiusIncrement, double startAngle, double endAngle) {
		Section section = new Section();
		section.setName(name);
		section.setPosition(position);
		section.setType(SectionType.SEATED);

		double currentRadius = startRadius;

		for(int i = 0; i < rows; i++) {
			Row row = new Row();
			row.setName(generateRowName(i));
			row.setPosition(Position.builder().x(0).y(0).build());

			ArcProperties arcProps = ArcProperties.builder()
					.centerX(0)
					.centerY(0)
					.radius(currentRadius)
					.startAngle(startAngle)
					.endAngle(endAngle)
					.build();

			row.setArcProperties(arcProps);
			row.generateArcSeats(seatsPerRow, arcProps);

			section.addRow(row);

			currentRadius += radiusIncrement;
		}
		// Đặt thuộc tính arc cho cả section
		ArcProperties sectionArcProps = ArcProperties.builder()
				.centerX(0)
				.centerY(0)
				.radius((startRadius + radiusIncrement) / 2)
				.startAngle(startAngle)
				.endAngle(endAngle)
				.build();
		section.setArcProperties(sectionArcProps);

		return section;
	}

	/**
	 * Tạo một section với các bàn
	 */
	public static Section createTableSection(String name, Position position, int tableRows, int tablesPerRow,
			TableShape tableShape, Dimension tableDimension, int seatsPerTable) {
		Section section = new Section();
		section.setName(name);
		section.setPosition(position);
		section.setType(SectionType.TABLE);

		double tableWidth = tableDimension.getWidth();
		double tableHeight = tableDimension.getHeight();
		double tableSpacingX = tableWidth * 1.5;
		double tableSpacingY = tableHeight * 1.5;

		for(int i = 0; i < tableRows; i++) {
			for(int j = 0; j < tablesPerRow; j++) {
				Table table = new Table();
				table.setName("T" + (i * tablesPerRow + j + 1));
				table.setPosition(new Position(j * tableSpacingX, i * tableSpacingY));
				table.setDimensions(tableDimension);
				table.setShape(tableShape);
				table.setCapacity(seatsPerTable);

				if(Objects.equals(tableShape, TableShape.ROUND)) {
					table.generateSeatsForRoundTable(seatsPerTable, Math.min(tableWidth, tableHeight) / 2);
				} else if(Objects.equals(tableShape, TableShape.RECTANGLE)) {
					int longSide = (int)Math.ceil(seatsPerTable / 4.0);
					int shortSide = (int)Math.floor(seatsPerTable / 4.0);
					table.generateSeatsForRectangleTable(longSide, shortSide);
				}
				section.addTable(table);
			}
		}
		return section;
	}

	/**
	 * Tạo một section khu vực đứng
	 */
	public static Section createStandingSection(String name, Position position, Dimension dimensions, int capacity) {
		Section section = new Section();
		section.setName(name);
		section.setPosition(position);
		section.setDimensions(dimensions);
		section.setType(SectionType.STANDING);
		section.setCapacity(capacity);

		return section;
	}

	private static String generateRowName(int row) {
		StringBuilder name = new StringBuilder();
		row++;
		while(row > 0) {
			row--; // Adjust for 0-based alphabet index (A = 0)
			char letter = (char)('A' + (row % 26));
			name.insert(0, letter);
			row /= 26;
		}
		return name.toString();
	}
}
