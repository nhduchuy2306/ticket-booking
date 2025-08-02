package com.gyp.eventservice.dtos.seatmap;

import java.util.Objects;

/**
 * Factory để tạo các loại layout khác nhau
 */
public final class SeatLayoutFactory {
	private SeatLayoutFactory() {
	}

	/**
	 * Template: Rạp phim/Nhà hát truyền thống
	 * Features: Slight curve, good sightlines, numbered seats
	 */
	public static VenueMap createTheaterTemplate(String venueName, Dimension venueDimensions, int totalRows,
			int seatsPerRow, boolean hasBalcony) {
		VenueMap venue = new VenueMap();
		venue.setName(venueName);
		venue.setDimensions(venueDimensions);

		// Stage setup - centered at front
		StageConfig stage = new StageConfig();
		stage.setName("Main Stage");
		stage.setPosition(new Position(venueDimensions.getWidth() / 2 - 50, 50));
		stage.setDimensions(new Dimension(100, 30));
		stage.setShape(StageShape.RECTANGLE);
		stage.setOrientation(StageOrientation.SOUTH);
		venue.setStageConfig(stage);

		SeatConfig seatConfig = new SeatConfig();

		// Main floor - slight arc for better viewing
		Section mainFloor = createArcSection(
				"Main Floor",
				new Position(50, 150),
				totalRows,
				seatsPerRow,
				150, // start radius
				25,  // radius increment per row
				160, // start angle (slight curve)
				20   // end angle
		);
		seatConfig.addSection(mainFloor);

		// Balcony if requested
		if(hasBalcony) {
			Section balcony = createArcSection(
					"Balcony",
					new Position(50, 100),
					totalRows / 2,
					seatsPerRow - 10,
					200, // higher radius
					20,
					150,
					30
			);
			seatConfig.addSection(balcony);
		}

		venue.setSeatConfig(seatConfig);
		return venue;
	}

	/**
	 * Template: Sân vận động
	 * Features: 360° seating, multiple tiers, large capacity
	 */
	public static VenueMap createStadiumTemplate(String venueName, Dimension venueDimensions, int sectionsCount,
			int rowsPerSection, int seatsPerRow, boolean hasVIPBoxes) {
		VenueMap venue = new VenueMap();
		venue.setName(venueName);
		venue.setDimensions(venueDimensions);

		// Center field/stage
		StageConfig stage = new StageConfig();
		stage.setName("Playing Field");
		stage.setPosition(new Position(venueDimensions.getWidth() / 2 - 100, venueDimensions.getHeight() / 2 - 75));
		stage.setDimensions(new Dimension(200, 150));
		stage.setShape(StageShape.RECTANGLE);
		venue.setStageConfig(stage);

		SeatConfig seatConfig = new SeatConfig();

		double centerX = venueDimensions.getWidth() / 2;
		double centerY = venueDimensions.getHeight() / 2;
		double anglePerSection = 360.0 / sectionsCount;

		// Create sections around the field
		for(int i = 0; i < sectionsCount; i++) {
			double startAngle = i * anglePerSection - anglePerSection / 2;
			double endAngle = startAngle + anglePerSection;

			Section section = createArcSection(
					"Section " + (i + 1),
					new Position(centerX, centerY),
					rowsPerSection,
					seatsPerRow,
					250, // start radius from center
					30,  // row spacing
					startAngle,
					endAngle
			);
			seatConfig.addSection(section);
		}

		// VIP Boxes
		if(hasVIPBoxes) {
			Section vipBoxes = createTableSection(
					"VIP Boxes",
					new Position(50, 50),
					2, 8, // 2 rows, 8 boxes per row
					TableShape.RECTANGLE,
					new Dimension(40, 30),
					6 // 6 seats per box
			);
			seatConfig.addSection(vipBoxes);
		}

		venue.setSeatConfig(seatConfig);
		return venue;
	}

	/**
	 * Template: Phòng hội nghị/Hội thảo
	 * Features: Simple rectangular layout, good visibility
	 */
	public static VenueMap createConferenceTemplate(String venueName, Dimension venueDimensions, int rows,
			int seatsPerRow, boolean hasVIPSection) {
		VenueMap venue = new VenueMap();
		venue.setName(venueName);
		venue.setDimensions(venueDimensions);

		// Presentation stage
		StageConfig stage = new StageConfig();
		stage.setName("Presentation Stage");
		stage.setPosition(new Position(venueDimensions.getWidth() / 2 - 75, 20));
		stage.setDimensions(new Dimension(150, 25));
		stage.setShape(StageShape.RECTANGLE);
		stage.setOrientation(StageOrientation.SOUTH);
		venue.setStageConfig(stage);

		SeatConfig seatConfig = new SeatConfig();

		// Main seating - linear rows
		Section mainSeating = createLinearSection(
				"Main Seating",
				new Position(100, 80),
				rows,
				seatsPerRow,
				40, // row spacing
				35  // seat spacing
		);
		seatConfig.addSection(mainSeating);

		// VIP front section
		if(hasVIPSection) {
			Section vipSection = createLinearSection(
					"VIP Section",
					new Position(150, 60),
					2,
					seatsPerRow - 4,
					45,
					40
			);
			seatConfig.addSection(vipSection);
		}

		venue.setSeatConfig(seatConfig);
		return venue;
	}

	/**
	 * Template: Hội trường/Concert Hall
	 * Features: Mix of seated and standing, premium acoustics layout
	 */
	public static VenueMap createConcertHallTemplate(String venueName, Dimension venueDimensions, int orchestraRows,
			int mezzanineRows, int standingCapacity) {
		VenueMap venue = new VenueMap();
		venue.setName(venueName);
		venue.setDimensions(venueDimensions);

		// Concert stage
		StageConfig stage = new StageConfig();
		stage.setName("Concert Stage");
		stage.setPosition(new Position(venueDimensions.getWidth() / 2 - 60, 30));
		stage.setDimensions(new Dimension(120, 40));
		stage.setShape(StageShape.SEMICIRCLE);
		stage.setOrientation(StageOrientation.SOUTH);
		venue.setStageConfig(stage);

		SeatConfig seatConfig = new SeatConfig();

		// Orchestra section (premium seats, close to stage)
		Section orchestra = createArcSection(
				"Orchestra",
				new Position(venueDimensions.getWidth() / 2, 100),
				orchestraRows,
				40,
				120, // closer to stage
				25,
				140,
				40
		);
		seatConfig.addSection(orchestra);

		// Mezzanine (elevated seating)
		Section mezzanine = createArcSection(
				"Mezzanine",
				new Position(venueDimensions.getWidth() / 2, 80),
				mezzanineRows,
				45,
				200, // further back, elevated view
				30,
				130,
				50
		);
		seatConfig.addSection(mezzanine);

		// Standing area (general admission)
		Section standing = createStandingSection(
				"General Standing",
				new Position(50, venueDimensions.getHeight() - 150),
				new Dimension(venueDimensions.getWidth() - 100, 100),
				standingCapacity
		);
		seatConfig.addSection(standing);

		venue.setSeatConfig(seatConfig);
		return venue;
	}

	/**
	 * Template: Arena (360° view)
	 * Features: Central stage, surrounding seating
	 */
	public static VenueMap createArenaTemplate(String venueName, Dimension venueDimensions, int ringCount,
			int sectionsPerRing, int rowsPerSection) {
		VenueMap venue = new VenueMap();
		venue.setName(venueName);
		venue.setDimensions(venueDimensions);

		// Central stage/ring
		double centerX = venueDimensions.getWidth() / 2;
		double centerY = venueDimensions.getHeight() / 2;

		StageConfig stage = new StageConfig();
		stage.setName("Central Arena");
		stage.setPosition(new Position(centerX - 50, centerY - 50));
		stage.setDimensions(new Dimension(100, 100));
		stage.setShape(StageShape.ARENA);
		venue.setStageConfig(stage);

		SeatConfig seatConfig = new SeatConfig();

		// Create concentric rings of seating
		for(int ring = 0; ring < ringCount; ring++) {
			double ringRadius = 150 + (ring * 80); // Base radius + ring spacing
			double anglePerSection = 360.0 / sectionsPerRing;

			for(int section = 0; section < sectionsPerRing; section++) {
				double startAngle = section * anglePerSection;
				double endAngle = startAngle + anglePerSection - 5; // Small gap between sections

				Section arenaSection = createArcSection(
						"Ring " + (ring + 1) + " Section " + (section + 1),
						new Position(centerX, centerY),
						rowsPerSection,
						calculateSeatsForArcSection(ringRadius, anglePerSection, rowsPerSection),
						ringRadius,
						25, // row depth
						startAngle,
						endAngle
				);
				seatConfig.addSection(arenaSection);
			}
		}

		venue.setSeatConfig(seatConfig);
		return venue;
	}

	/**
	 * Template: Wedding/Banquet
	 * Features: Round tables, social seating arrangement
	 */
	public static VenueMap createBanquetTemplate(String venueName, Dimension venueDimensions, int tableRows,
			int tablesPerRow, int guestsPerTable, boolean hasHeadTable) {
		VenueMap venue = new VenueMap();
		venue.setName(venueName);
		venue.setDimensions(venueDimensions);

		SeatConfig seatConfig = new SeatConfig();

		// Main table seating
		Section mainTables = createTableSection(
				"Guest Tables",
				new Position(100, 150),
				tableRows,
				tablesPerRow,
				TableShape.ROUND,
				new Dimension(120, 120), // Round table diameter
				guestsPerTable
		);
		seatConfig.addSection(mainTables);

		// Head table for VIPs
		if(hasHeadTable) {
			Section headTable = createTableSection(
					"Head Table",
					new Position(venueDimensions.getWidth() / 2 - 150, 50),
					1, 1, // Single long table
					TableShape.RECTANGLE,
					new Dimension(300, 80),
					12 // Seats along head table
			);
			seatConfig.addSection(headTable);
		}

		// Dance floor (standing area)
		Section danceFloor = createStandingSection(
				"Dance Floor",
				new Position(venueDimensions.getWidth() / 2 - 75, venueDimensions.getHeight() / 2 - 75),
				new Dimension(150, 150),
				100 // Standing capacity
		);
		seatConfig.addSection(danceFloor);

		venue.setSeatConfig(seatConfig);
		return venue;
	}

	/**
	 * Template: Classroom/Training Room
	 * Features: Simple grid, focus on instructor area
	 */
	public static VenueMap createClassroomTemplate(String venueName, Dimension venueDimensions, int rows,
			int seatsPerRow, boolean hasGroupTables) {
		VenueMap venue = new VenueMap();
		venue.setName(venueName);
		venue.setDimensions(venueDimensions);

		// Teaching area
		StageConfig stage = new StageConfig();
		stage.setName("Teaching Area");
		stage.setPosition(new Position(50, 20));
		stage.setDimensions(new Dimension(venueDimensions.getWidth() - 100, 60));
		stage.setShape(StageShape.RECTANGLE);
		venue.setStageConfig(stage);

		SeatConfig seatConfig = new SeatConfig();

		if(hasGroupTables) {
			// Group table setup for collaborative learning
			Section groupTables = createTableSection(
					"Group Tables",
					new Position(80, 120),
					rows / 2,
					seatsPerRow / 3,
					TableShape.RECTANGLE,
					new Dimension(120, 80),
					6 // 6 people per group table
			);
			seatConfig.addSection(groupTables);
		} else {
			// Traditional classroom rows
			Section classroomSeats = createLinearSection(
					"Student Seating",
					new Position(80, 120),
					rows,
					seatsPerRow,
					50, // Wide row spacing for movement
					40  // Comfortable seat spacing
			);
			seatConfig.addSection(classroomSeats);
		}

		venue.setSeatConfig(seatConfig);
		return venue;
	}

	// ==================== HELPER METHODS ====================

	/**
	 * Calculate optimal number of seats for an arc section based on geometry
	 */
	private static int calculateSeatsForArcSection(double radius, double angleRange, int rows) {
		double arcLength = Math.toRadians(angleRange) * radius;
		int baseSeats = (int)Math.floor(arcLength / 40); // 40cm per seat roughly
		return Math.max(baseSeats, 10); // Minimum 10 seats per section
	}

	/**
	 * Generate optimal venue dimensions based on capacity and type
	 */
	public static Dimension calculateOptimalVenueDimensions(String templateType, int expectedCapacity) {
		return switch(templateType.toUpperCase()) {
			case "THEATER" ->
					new Dimension(Math.max(600, expectedCapacity * 0.8), Math.max(400, expectedCapacity * 0.6));
			case "STADIUM" ->
					new Dimension(Math.max(800, expectedCapacity * 0.5), Math.max(800, expectedCapacity * 0.5));
			case "CONFERENCE" ->
					new Dimension(Math.max(400, expectedCapacity * 1.2), Math.max(300, expectedCapacity * 0.8));
			case "CONCERT" ->
					new Dimension(Math.max(500, expectedCapacity * 0.9), Math.max(400, expectedCapacity * 0.7));
			case "ARENA" -> new Dimension(Math.max(600, expectedCapacity * 0.6), Math.max(600, expectedCapacity * 0.6));
			case "BANQUET" ->
					new Dimension(Math.max(500, expectedCapacity * 1.5), Math.max(400, expectedCapacity * 1.2));
			case "CLASSROOM" ->
					new Dimension(Math.max(300, expectedCapacity * 2.0), Math.max(200, expectedCapacity * 1.5));
			default -> new Dimension(500, 400);
		};
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
