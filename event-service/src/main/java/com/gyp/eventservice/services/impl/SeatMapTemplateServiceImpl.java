package com.gyp.eventservice.services.impl;

import com.gyp.eventservice.dtos.seatmap.ArcProperties;
import com.gyp.eventservice.dtos.seatmap.Dimension;
import com.gyp.eventservice.dtos.seatmap.Position;
import com.gyp.eventservice.dtos.seatmap.Row;
import com.gyp.eventservice.dtos.seatmap.SeatConfig;
import com.gyp.eventservice.dtos.seatmap.Section;
import com.gyp.eventservice.dtos.seatmap.SectionType;
import com.gyp.eventservice.dtos.seatmap.StageConfig;
import com.gyp.eventservice.dtos.seatmap.StageOrientation;
import com.gyp.eventservice.dtos.seatmap.StageShape;
import com.gyp.eventservice.dtos.seatmap.Table;
import com.gyp.eventservice.dtos.seatmap.TableShape;
import com.gyp.eventservice.dtos.seatmap.VenueMap;
import com.gyp.eventservice.services.SeatMapTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SeatMapTemplateServiceImpl implements SeatMapTemplateService {
	/**
	 * Template for theater-style seating
	 */
	@Override
	public VenueMap createTheaterTemplate(String name, int totalRows, int seatsPerRow) {
		VenueMap venueMap = VenueMap.builder()
				.name(name)
				.dimensions(new Dimension(1080, 860))
				.seatConfig(new SeatConfig())
				.build();

		// Create stage configuration
		StageConfig stage = StageConfig.builder()
				.name("Main Stage")
				.position(new Position(340, 60))
				.dimensions(new Dimension(400, 110))
				.shape(StageShape.RECTANGLE)
				.orientation(StageOrientation.SOUTH)
				.build();

		venueMap.setStageConfig(stage);

		// Create theater sections for different levels
		createTheaterSections(venueMap, totalRows, seatsPerRow);

		return venueMap;
	}

	private void createTheaterSections(VenueMap venueMap, int totalRows, int seatsPerRow) {
		createCurvedTheaterSection(venueMap, "Orchestra", new Position(540, 190),
				new Dimension(640, 260), totalRows * 2 / 3, seatsPerRow, 20, 160, 150, 34, "orchestra");
		createCurvedTheaterSection(venueMap, "Mezzanine", new Position(540, 260),
				new Dimension(780, 320), Math.max(2, totalRows / 3), seatsPerRow + 4, 20, 160, 210, 30, "mezzanine");
		createCurvedTheaterSection(venueMap, "Balcony", new Position(540, 340),
				new Dimension(920, 380), Math.max(2, totalRows / 4), seatsPerRow + 8, 20, 160, 280, 28, "balcony");
	}

	private void createCurvedTheaterSection(VenueMap venueMap, String name, Position center,
			Dimension dimensions, int rows, int seatsPerRow, double startAngle, double endAngle,
			double innerRadius, double rowSpacing, String ticketTypeId) {
		Section section = Section.builder()
				.name(name)
				.type(SectionType.SEATED)
				.position(center)
				.dimensions(dimensions)
				.isArc(true)
				.ticketTypeId(ticketTypeId)
				.build();

		section.setArcProperties(ArcProperties.builder()
				.centerX(center.getX())
				.centerY(center.getY())
				.radius(innerRadius + rows * rowSpacing)
				.startAngle(startAngle)
				.endAngle(endAngle)
				.build());

		for(int i = 0; i < rows; i++) {
			Row row = Row.builder()
					.name("Row " + (char)('A' + i))
					.position(center)
					.isArc(true)
					.build();

			ArcProperties rowArcProps = ArcProperties.builder()
					.centerX(center.getX())
					.centerY(center.getY())
					.radius(innerRadius + i * rowSpacing)
					.startAngle(startAngle)
					.endAngle(endAngle)
					.build();

			row.generateArcSeats(seatsPerRow, rowArcProps);
			section.addRow(row);
		}

		venueMap.getSeatConfig().addSection(section);
	}

	/**
	 * Template for stadium concert with optional field
	 */
	@Override
	public VenueMap createStadiumConcertTemplate(String name, boolean includeField) {
		VenueMap venueMap = VenueMap.builder()
				.name(name)
				.dimensions(new Dimension(2000, 1500))
				.seatConfig(new SeatConfig())
				.build();

		// Sân khấu ở giữa sân
		StageConfig stage = StageConfig.builder()
				.name("Main Stage")
				.position(new Position(900, 700))
				.dimensions(new Dimension(200, 100))
				.shape(StageShape.RECTANGLE)
				.orientation(StageOrientation.NORTH)
				.build();

		venueMap.setStageConfig(stage);

		createStadiumSections(venueMap, includeField);

		return venueMap;
	}

	private void createStadiumSections(VenueMap venueMap, boolean includeField) {
		if(includeField) {
			// GA Field (standing)
			Section field = Section.builder()
					.name("GA Field")
					.type(SectionType.STANDING)
					.position(new Position(600, 500))
					.dimensions(new Dimension(800, 500))
					.capacity(5000)
					.ticketTypeId("field")
					.build();

			venueMap.getSeatConfig().addSection(field);
		}

		// Lower Bowl
		createStadiumBowl(venueMap, "Lower Bowl", new Position(300, 300), 25, 40, "lower");

		// Upper Bowl
		createStadiumBowl(venueMap, "Upper Bowl", new Position(200, 200), 35, 50, "upper");

		// VIP Boxes
		createVIPBoxes(venueMap);
	}

	private void createStadiumBowl(VenueMap venueMap, String sectionName, Position basePosition,
			int rows, int seatsPerRow, String ticketTypeId) {
		// Tạo 4 khu vực (North, South, East, West)
		String[] directions = { "North", "South", "East", "West" };
		Position[] positions = {
				new Position(basePosition.getX(), basePosition.getY() - 100),
				new Position(basePosition.getX(), basePosition.getY() + 600),
				new Position(basePosition.getX() + 800, basePosition.getY() + 200),
				new Position(basePosition.getX() - 200, basePosition.getY() + 200)
		};

		for(int i = 0; i < directions.length; i++) {
			Section section = Section.builder()
					.name(sectionName + " " + directions[i])
					.type(SectionType.SEATED)
					.position(positions[i])
					.ticketTypeId(ticketTypeId)
					.build();

			createRowsForSection(section, rows, seatsPerRow, 35, 32);
			venueMap.getSeatConfig().addSection(section);
		}
	}

	/**
	 * Template for conference with round tables
	 */
	@Override
	public VenueMap createConferenceTemplate(String name, int numberOfTables, int seatsPerTable) {
		VenueMap venueMap = VenueMap.builder()
				.name(name)
				.dimensions(new Dimension(1200, 900))
				.seatConfig(new SeatConfig())
				.build();

		// Sân khấu/podium ở phía trước
		StageConfig stage = StageConfig.builder()
				.name("Podium")
				.position(new Position(500, 50))
				.dimensions(new Dimension(200, 80))
				.shape(StageShape.RECTANGLE)
				.orientation(StageOrientation.SOUTH)
				.build();

		venueMap.setStageConfig(stage);

		// Tạo khu vực bàn
		Section tableSection = Section.builder()
				.name("Dining Area")
				.type(SectionType.TABLE)
				.position(new Position(100, 200))
				.ticketTypeId("table")
				.build();

		createRoundTables(tableSection, numberOfTables, seatsPerTable);
		venueMap.getSeatConfig().addSection(tableSection);

		return venueMap;
	}

	private void createRoundTables(Section section, int numberOfTables, int seatsPerTable) {
		int tablesPerRow = (int)Math.ceil(Math.sqrt(numberOfTables));
		double tableSpacing = 150;

		for(int i = 0; i < numberOfTables; i++) {
			int row = i / tablesPerRow;
			int col = i % tablesPerRow;

			Table table = Table.builder()
					.name("Table " + (i + 1))
					.position(new Position(col * tableSpacing, row * tableSpacing))
					.dimensions(new Dimension(120, 120))
					.shape(TableShape.ROUND)
					.capacity(seatsPerTable)
					.build();

			table.generateSeatsForRoundTable(seatsPerTable, 60);
			section.addTable(table);
		}
	}

	/**
	 * Template for arena concert (circular stage)
	 */
	@Override
	public VenueMap createArenaConcertTemplate(String name) {
		VenueMap venueMap = VenueMap.builder()
				.name(name)
				.dimensions(new Dimension(1500, 1200))
				.seatConfig(new SeatConfig())
				.build();

		// Sân khấu tròn ở giữa
		StageConfig stage = StageConfig.builder()
				.name("Center Stage")
				.position(new Position(650, 550))
				.dimensions(new Dimension(200, 100))
				.shape(StageShape.CIRCULAR)
				.orientation(StageOrientation.NORTH)
				.build();

		venueMap.setStageConfig(stage);

		createArenaSections(venueMap);

		return venueMap;
	}

	private void createArenaSections(VenueMap venueMap) {
		// Floor sections (surrounding stage)
		createArenaFloorSections(venueMap);

		// Lower Bowl (arc sections)
		createArenaArcSections(venueMap, "Lower Bowl", 400, 600, 20, 25, "lower");

		// Upper Bowl
		createArenaArcSections(venueMap, "Upper Bowl", 300, 700, 30, 35, "upper");
	}

	private void createArenaFloorSections(VenueMap venueMap) {
		// 4 floor sections around the stage
		String[] sectionNames = { "Floor A", "Floor B", "Floor C", "Floor D" };
		Position[] positions = {
				new Position(750, 400),  // North
				new Position(900, 600),  // East
				new Position(750, 800),  // South
				new Position(600, 600)   // West
		};

		for(int i = 0; i < sectionNames.length; i++) {
			Section section = Section.builder()
					.name(sectionNames[i])
					.type(SectionType.SEATED)
					.position(positions[i])
					.ticketTypeId("floor")
					.build();

			createRowsForSection(section, 8, 12, 35, 32);
			venueMap.getSeatConfig().addSection(section);
		}
	}

	private void createArenaArcSections(VenueMap venueMap, String levelName, double innerRadius,
			double outerRadius, int rows, int seatsPerRow, String ticketTypeId) {
		// Tạo 8 sections arc xung quanh sân khấu
		for(int i = 0; i < 8; i++) {
			double startAngle = i * 45;
			double endAngle = startAngle + 40; // Để lại khoảng trống giữa các section

			Section section = Section.builder()
					.name(levelName + " Section " + (i + 1))
					.type(SectionType.SEATED)
					.position(new Position(750, 600)) // Center position
					.isArc(true)
					.ticketTypeId(ticketTypeId)
					.build();

			ArcProperties arcProps = ArcProperties.builder()
					.centerX(0)
					.centerY(0)
					.radius(innerRadius + (outerRadius - innerRadius) / 2)
					.startAngle(startAngle)
					.endAngle(endAngle)
					.build();

			section.setArcProperties(arcProps);

			// Tạo rows theo arc
			for(int rowIndex = 0; rowIndex < rows; rowIndex++) {
				Row row = Row.builder()
						.name("Row " + (char)('A' + rowIndex))
						.position(new Position(0, 0))
						.isArc(true)
						.build();

				double rowRadius = innerRadius + rowIndex * (outerRadius - innerRadius) / rows;
				ArcProperties rowArcProps = ArcProperties.builder()
						.centerX(0)
						.centerY(0)
						.radius(rowRadius)
						.startAngle(startAngle)
						.endAngle(endAngle)
						.build();

				row.generateArcSeats(seatsPerRow, rowArcProps);
				section.addRow(row);
			}

			venueMap.getSeatConfig().addSection(section);
		}
	}

	/**
	 * Template for small club and venue
	 */
	@Override
	public VenueMap createClubTemplate(String name, int standingCapacity) {
		VenueMap venueMap = VenueMap.builder()
				.name(name)
				.dimensions(new Dimension(800, 600))
				.seatConfig(new SeatConfig())
				.build();

		// Sân khấu nhỏ
		StageConfig stage = StageConfig.builder()
				.name("Stage")
				.position(new Position(300, 50))
				.dimensions(new Dimension(200, 80))
				.shape(StageShape.RECTANGLE)
				.orientation(StageOrientation.SOUTH)
				.build();

		venueMap.setStageConfig(stage);

		// GA Standing area
		Section gaSection = Section.builder()
				.name("General Admission")
				.type(SectionType.STANDING)
				.position(new Position(100, 200))
				.dimensions(new Dimension(600, 300))
				.capacity(standingCapacity)
				.ticketTypeId("ga")
				.build();

		venueMap.getSeatConfig().addSection(gaSection);

		// VIP Table area
		Section vipSection = Section.builder()
				.name("VIP Tables")
				.type(SectionType.TABLE)
				.position(new Position(50, 520))
				.ticketTypeId("vip")
				.build();

		createRoundTables(vipSection, 6, 4);
		venueMap.getSeatConfig().addSection(vipSection);

		return venueMap;
	}

	private void createVIPBoxes(VenueMap venueMap) {
		Section vipBoxes = Section.builder()
				.name("VIP Boxes")
				.type(SectionType.SEATED)
				.position(new Position(100, 100))
				.ticketTypeId("vip")
				.build();

		// Tạo 10 VIP boxes
		for(int i = 0; i < 10; i++) {
			Row box = Row.builder()
					.name("Box " + (i + 1))
					.position(new Position(i * 80, 0))
					.build();

			box.generateLinearSeats(6, 0, 25);
			vipBoxes.addRow(box);
		}

		venueMap.getSeatConfig().addSection(vipBoxes);
	}

	private void createRowsForSection(Section section, int numberOfRows, int seatsPerRow,
			double rowSpacing, double seatSpacing) {
		for(int i = 0; i < numberOfRows; i++) {
			Row row = Row.builder()
					.name("Row " + (char)('A' + i))
					.position(new Position(0, i * rowSpacing))
					.build();

			row.generateLinearSeats(seatsPerRow, 0, seatSpacing);
			section.addRow(row);
		}
	}
}
