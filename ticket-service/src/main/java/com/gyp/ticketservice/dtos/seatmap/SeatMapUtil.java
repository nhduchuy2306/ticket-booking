package com.gyp.ticketservice.dtos.seatmap;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public final class SeatMapUtil {
	private SeatMapUtil() {
	}

	/**
	 * Tính toán vị trí tuyệt đối của một ghế
	 */
	public static Position calculateAbsoluteSeatPosition(VenueMap venueMap, String sectionId, String rowId,
			String seatId) {
		Section section = venueMap.getSeatConfig().getSections().stream()
				.filter(s -> s.getId().equals(sectionId))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Section not found"));

		Row row = section.getRows().stream()
				.filter(r -> r.getId().equals(rowId))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Row not found"));

		Seat seat = getSeatById(row, seatId);

		if(seat.isUseAbsolutePosition()) {
			return seat.getAbsolutePosition();
		}

		Position sectionPos = section.getPosition();
		Position rowPos = row.getPosition();
		Position seatPos = seat.getPosition();

		// Handle Arc
		if(row.getIsArc()) {
			ArcProperties arcProps = row.getArcProperties();
			double seatIndex = Double.parseDouble(seat.getName()) - 1;
			double totalSeats = row.getSeats().size();
			double angleRange = arcProps.getEndAngle() - arcProps.getStartAngle();
			double angle = arcProps.getStartAngle() + (seatIndex / (totalSeats - 1)) * angleRange;

			seatPos = arcProps.calculatePointOnArc(angle);
		}

		// Calculate absolute position
		double absoluteX = sectionPos.getX() + rowPos.getX() + seatPos.getX();
		double absoluteY = sectionPos.getY() + rowPos.getY() + seatPos.getY();

		// Calculate if section can rotate
		if(section.getRotation() != 0) {
			double radians = Math.toRadians(section.getRotation());
			double cosTheta = Math.cos(radians);
			double sinTheta = Math.sin(radians);

			// Tọa độ tương đối so với điểm gốc của section
			double relativeX = rowPos.getX() + seatPos.getX();
			double relativeY = rowPos.getY() + seatPos.getY();

			// Áp dụng phép xoay
			double rotatedX = relativeX * cosTheta - relativeY * sinTheta;
			double rotatedY = relativeX * sinTheta + relativeY * cosTheta;

			// Tính lại tọa độ tuyệt đối
			absoluteX = sectionPos.getX() + rotatedX;
			absoluteY = sectionPos.getY() + rotatedY;
		}
		return Position.builder().x(absoluteX).y(absoluteY).build();
	}

	/**
	 * Calculate the absolute position of seat in table
	 */
	public static Position calculateAbsoluteSeatPositionInTable(VenueMap venueMap, String sectionId, String tableId,
			String seatId) {
		Section section = venueMap.getSeatConfig().getSections().stream()
				.filter(s -> s.getId().equals(sectionId))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Section not found"));

		Table table = section.getTables().stream()
				.filter(t -> t.getId().equals(tableId))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Table not found"));

		Seat seat = getSeatById(table, seatId);

		if(seat.isUseAbsolutePosition()) {
			return seat.getAbsolutePosition();
		}

		Position sectionPos = section.getPosition();
		Position tablePos = table.getPosition();
		Position seatPos = seat.getPosition();

		// Tính toán vị trí tuyệt đối
		double absoluteX = sectionPos.getX() + tablePos.getX() + seatPos.getX();
		double absoluteY = sectionPos.getY() + tablePos.getY() + seatPos.getY();

		return new Position(absoluteX, absoluteY);
	}

	/**
	 * Create a copy of venue map for the specific event
	 */
	public static VenueMap createEventSpecificVenueMap(VenueMap originalMap, String eventId) {
		// Deep copy venue map
		VenueMap eventMap = new VenueMap();
		eventMap.setName(originalMap.getName() + " - Event " + eventId);
		eventMap.setDimensions(new Dimension(
				originalMap.getDimensions().getWidth(),
				originalMap.getDimensions().getHeight()
		));

		// Deep copy Stage
		eventMap.setStageConfig(SeatMapUtil.deepCopyStageConfig(originalMap.getStageConfig()));

		// Deep copy SeatConfig
		eventMap.setSeatConfig(SeatMapUtil.deepCopySeatConfig(originalMap.getSeatConfig()));

		return eventMap;
	}

	private static StageConfig deepCopyStageConfig(StageConfig original) {
		StageConfig cloned = new StageConfig();
		cloned.setId(UUID.randomUUID().toString());
		cloned.setName(original.getName());
		cloned.setDescription(original.getDescription());
		cloned.setRotation(original.getRotation());
		cloned.setShape(original.getShape());
		cloned.setOrientation(original.getOrientation());
		cloned.setActive(original.isActive());
		cloned.setCustomVertices(
				original.getCustomVertices() == null ? null : original.getCustomVertices()
		);
		cloned.setElevation(original.getElevation());
		cloned.setSvgPath(original.getSvgPath());

		// Deep copy position
		if(original.getPosition() != null) {
			cloned.setPosition(new Position(
					original.getPosition().getX(),
					original.getPosition().getY()
			));
		}

		// Deep copy dimensions
		if(original.getDimensions() != null) {
			cloned.setDimensions(new Dimension(
					original.getDimensions().getWidth(),
					original.getDimensions().getHeight()
			));
		}

		return cloned;
	}

	/**
	 * Deep copy SeatConfig
	 */
	private static SeatConfig deepCopySeatConfig(SeatConfig seatConfig) {
		SeatConfig cloned = new SeatConfig();
		cloned.setSeatTypes(seatConfig.getSeatTypes());

		// Deep copy sections
		List<Section> clonedSections = seatConfig.getSections()
				.stream()
				.map(SeatMapUtil::deepCopySection)
				.toList();
		cloned.setSections(clonedSections);

		return cloned;
	}

	/**
	 * Deep copy one Section
	 */
	private static Section deepCopySection(Section original) {
		Section cloned = new Section();

		// Copy basic properties
		cloned.setId(UUID.randomUUID().toString()); // Tạo ID mới
		cloned.setName(original.getName());
		cloned.setType(original.getType());
		cloned.setCapacity(original.getCapacity());
		cloned.setRotation(original.getRotation());

		// Deep copy position
		if(original.getPosition() != null) {
			cloned.setPosition(new Position(
					original.getPosition().getX(),
					original.getPosition().getY()
			));
		}

		// Deep copy dimensions
		if(original.getDimensions() != null) {
			cloned.setDimensions(new Dimension(
					original.getDimensions().getWidth(),
					original.getDimensions().getHeight()
			));
		}

		// Deep copy arc properties
		if(original.getArcProperties() != null) {
			ArcProperties originalArc = original.getArcProperties();
			cloned.setArcProperties(new ArcProperties(
					originalArc.getCenterX(),
					originalArc.getCenterY(),
					originalArc.getRadius(),
					originalArc.getStartAngle(),
					originalArc.getEndAngle(),
					originalArc.getThickness()
			));
		}

		// Deep copy rows
		if(original.getRows() != null) {
			List<Row> clonedRows = original.getRows()
					.stream()
					.map(SeatMapUtil::deepCopyRow)
					.collect(Collectors.toList());
			cloned.setRows(clonedRows);
		}

		// Deep copy tables
		if(original.getTables() != null) {
			List<Table> clonedTables = original.getTables()
					.stream()
					.map(SeatMapUtil::deepCopyTable)
					.collect(Collectors.toList());
			cloned.setTables(clonedTables);
		}

		return cloned;
	}

	/**
	 * Deep copy one Row
	 */
	private static Row deepCopyRow(Row original) {
		Row cloned = new Row();

		// Copy basic properties
		cloned.setId(UUID.randomUUID().toString()); // Tạo ID mới
		cloned.setName(original.getName());

		// Deep copy position
		if(original.getPosition() != null) {
			cloned.setPosition(new Position(
					original.getPosition().getX(),
					original.getPosition().getY()
			));
		}

		// Deep copy arc properties
		if(original.getArcProperties() != null) {
			ArcProperties originalArc = original.getArcProperties();
			cloned.setArcProperties(new ArcProperties(
					originalArc.getCenterX(),
					originalArc.getCenterY(),
					originalArc.getRadius(),
					originalArc.getStartAngle(),
					originalArc.getEndAngle(),
					originalArc.getThickness()
			));
		}

		// Deep copy seats
		if(original.getSeats() != null) {
			List<Seat> clonedSeats = original.getSeats()
					.stream()
					.map(SeatMapUtil::deepCopySeat)
					.collect(Collectors.toList());
			cloned.setSeats(clonedSeats);
		}

		return cloned;
	}

	/**
	 * Deep copy one Table
	 */
	private static Table deepCopyTable(Table original) {
		Table cloned = new Table();

		// Copy basic properties
		cloned.setId(UUID.randomUUID().toString()); // Tạo ID mới
		cloned.setName(original.getName());
		cloned.setShape(original.getShape());
		cloned.setCapacity(original.getCapacity());

		// Deep copy position
		if(original.getPosition() != null) {
			cloned.setPosition(new Position(
					original.getPosition().getX(),
					original.getPosition().getY()
			));
		}

		// Deep copy dimensions
		if(original.getDimensions() != null) {
			cloned.setDimensions(new Dimension(
					original.getDimensions().getWidth(),
					original.getDimensions().getHeight()
			));
		}

		// Deep copy seats
		if(original.getSeats() != null) {
			List<Seat> clonedSeats = original.getSeats()
					.stream()
					.map(SeatMapUtil::deepCopySeat)
					.collect(Collectors.toList());
			cloned.setSeats(clonedSeats);
		}

		return cloned;
	}

	/**
	 * Deep copy one Seat
	 */
	private static Seat deepCopySeat(Seat original) {
		Seat cloned = new Seat();

		// Copy basic properties
		cloned.setId(UUID.randomUUID().toString()); // Tạo ID mới
		cloned.setName(original.getName());
		cloned.setStatus(SeatStatus.AVAILABLE); // Reset về trạng thái available cho event mới
		cloned.setUseAbsolutePosition(original.isUseAbsolutePosition());

		// Deep copy position
		if(original.getPosition() != null) {
			cloned.setPosition(new Position(
					original.getPosition().getX(),
					original.getPosition().getY()
			));
		}

		// Deep copy absolute position
		if(original.getAbsolutePosition() != null) {
			cloned.setAbsolutePosition(new Position(
					original.getAbsolutePosition().getX(),
					original.getAbsolutePosition().getY()
			));
		}

		// Deep copy attributes using Apache Commons Lang
		if(original.getAttributes() != null) {
			Map<String, Boolean> clonedAttributes = new HashMap<>(original.getAttributes());
			cloned.setAttributes(clonedAttributes);
		}

		return cloned;
	}

	/**
	 * Find the adjacent seat in row
	 */
	public static List<Seat> findAdjacentSeats(Row row, int count) {
		List<Seat> seats = row.getSeats();
		List<Seat> result = new ArrayList<>();

		// Tìm nhóm ghế liên tiếp có trạng thái AVAILABLE
		for(int i = 0; i <= seats.size() - count; i++) {
			boolean allAvailable = true;
			List<Seat> adjacentSeats = new ArrayList<>();

			for(int j = 0; j < count; j++) {
				Seat seat = seats.get(i + j);
				if(!Objects.equals(seat.getStatus(), SeatStatus.AVAILABLE)) {
					allAvailable = false;
					break;
				}
				adjacentSeats.add(seat);
			}

			if(allAvailable) {
				return adjacentSeats;
			}
		}

		return result;
	}

	/**
	 * Finds the best group of adjacent seats in a section based on proximity to the center.
	 */
	public static List<Seat> findBestSeats(Section section, int count) {
		// Calculate the horizontal center point of the section
		double centerX = section.getDimensions().getWidth() / 2;

		// Copy and sort the rows from front to back (closer to the stage first)
		List<Row> sortedRows = new ArrayList<>(section.getRows());
		sortedRows.sort(Comparator.comparingDouble(r -> r.getPosition().getY()));

		// Prioritize rows that are closer to the center (vertically)
		for(Row row : sortedRows) {
			List<Seat> seats = row.getSeats();

			// Proceed only if the row has at least as many seats as requested
			if(count <= seats.size()) {
				// Sort seats in the row by distance to the horizontal center
				List<Seat> sortedSeats = new ArrayList<>(seats);
				sortedSeats.sort((s1, s2) -> {
					double d1 = Math.abs(s1.getPosition().getX() - centerX);
					double d2 = Math.abs(s2.getPosition().getX() - centerX);
					return Double.compare(d1, d2);
				});

				// Try to find a group of adjacent seats closest to the center
				List<Seat> bestSeats = findAdjacentSeats(row, count);
				if(!bestSeats.isEmpty()) {
					return bestSeats;
				}
			}
		}
		return new ArrayList<>();
	}

	/**
	 * Tìm ghế tôt nhất dựa trên vị trí sân khấu
	 */
	public static List<Seat> findBestSeatsBasedOnStage(VenueMap venueMap, int count) {
		StageConfig stageConfig = venueMap.getStageConfig();
		if(stageConfig == null) {
			return new ArrayList<>();
		}

		List<SeatWithScore> seatsWithScores = new ArrayList<>();

		SeatConfig seatConfig = Optional.of(venueMap.getSeatConfig()).orElse(new SeatConfig());

		// Thu thập tất cả ghế và tính điểm
		for(Section section : seatConfig.getSections()) {
			if(Objects.equals(section.getType(), SectionType.SEATED)) {
				for(Row row : section.getRows()) {
					for(Seat seat : row.getSeats()) {
						if(Objects.equals(seat.getStatus(), SeatStatus.AVAILABLE)) {
							Position seatPos = calculateAbsoluteSeatPosition(
									venueMap, section.getId(), row.getId(), seat.getId()
							);
							double score = stageConfig.calculateSeatQualityScore(seatPos);
							seatsWithScores.add(new SeatWithScore(seat, score));
						}
					}
				}
			}
		}

		// Sắp xếp theo điểm từ cao đến thấp
		seatsWithScores.sort((s1, s2) -> Double.compare(s2.getScore(), s1.getScore()));

		// Trả về top ghế
		return seatsWithScores.stream()
				.limit(count)
				.map(SeatWithScore::getSeat)
				.collect(Collectors.toList());
	}

	/**
	 * Find seat base on seatId
	 */
	private static Seat getSeatById(SeatContainer seatContainer, String seatId) {
		return seatContainer.getSeats().stream()
				.filter(s -> s.getId().equals(seatId))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Seat not found"));
	}
}
