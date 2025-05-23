package com.gyp.eventservice.dtos.seatmap2;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
		Section section = venueMap.getSections().stream()
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

		// Xử lý vòng cung nếu cần
		if(row.isArc()) {
			// Nếu là hàng vòng cung, tính toán vị trí trên vòng cung
			ArcProperties arcProps = row.getArcProperties();
			double seatIndex = Double.parseDouble(seat.getName()) - 1;
			double totalSeats = row.getSeats().size();
			double angleRange = arcProps.getEndAngle() - arcProps.getStartAngle();
			double angle = arcProps.getStartAngle() + (seatIndex / (totalSeats - 1)) * angleRange;

			seatPos = arcProps.calculatePointOnArc(angle);
		}

		// Tính toán vị trí tuyệt đối
		double absoluteX = sectionPos.getX() + rowPos.getX() + seatPos.getX();
		double absoluteY = sectionPos.getY() + rowPos.getY() + seatPos.getY();

		// Nếu section được xoay, cần tính toán thêm
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

		return new Position(absoluteX, absoluteY);
	}

	/**
	 * Tính toán vị trí tuyệt đối của một ghế trong bàn
	 */
	public static Position calculateAbsoluteSeatPositionInTable(VenueMap venueMap, String sectionId, String tableId,
			String seatId) {
		Section section = venueMap.getSections().stream()
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
	 * Tạo một bản sao của venue map cho sự kiện cụ thể
	 */
	public static VenueMap createEventSpecificVenueMap(VenueMap originalMap, String eventId) {
		// Deep copy venue map
		VenueMap eventMap = new VenueMap();
		eventMap.setName(originalMap.getName() + " - Event " + eventId);
		eventMap.setDimensions(new Dimension(
				originalMap.getDimensions().getWidth(),
				originalMap.getDimensions().getHeight()
		));

		// Deep copy sections
		List<Section> clonedSections = originalMap.getSections()
				.stream()
				.map(SeatMapUtil::deepCopySection)
				.collect(Collectors.toList());
		eventMap.setSections(clonedSections);

		return eventMap;
	}

	/**
	 * Deep copy một Section
	 */
	private static Section deepCopySection(Section original) {
		Section cloned = new Section();

		// Copy basic properties
		cloned.setId(UUID.randomUUID().toString()); // Tạo ID mới
		cloned.setName(original.getName());
		cloned.setType(original.getType());
		cloned.setCapacity(original.getCapacity());
		cloned.setPriceCategoryId(original.getPriceCategoryId());
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
					originalArc.getEndAngle()
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
	 * Deep copy một Row
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
					originalArc.getEndAngle()
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
	 * Deep copy một Table
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
	 * Deep copy một Seat
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
	 * Tìm ghế lân cận trong một hàng
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
	 * Tìm ghế tốt nhất theo một hàng và khoảng cách đến trung tâm
	 */
	public static List<Seat> findBestSeats(Section section, int count) {
		// Tính toán điểm trung tâm của section
		double centerX = section.getDimensions().getWidth() / 2;

		// Sắp xếp các hàng từ trước ra sau (thường là từ trước sân khấu)
		List<Row> sortedRows = new ArrayList<>(section.getRows());
		sortedRows.sort(Comparator.comparingDouble(r -> r.getPosition().getY()));

		// Ưu tiên các hàng ở giữa
		for(Row row : sortedRows) {
			List<Seat> seats = row.getSeats();

			// Nếu số ghế cần ít hơn số ghế có sẵn trong hàng
			if(count <= seats.size()) {
				// Sắp xếp ghế theo khoảng cách đến trung tâm
				List<Seat> sortedSeats = new ArrayList<>(seats);
				sortedSeats.sort((s1, s2) -> {
					double d1 = Math.abs(s1.getPosition().getX() - centerX);
					double d2 = Math.abs(s2.getPosition().getX() - centerX);
					return Double.compare(d1, d2);
				});

				// Chọn nhóm ghế liên tiếp gần trung tâm nhất
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
		Stage primaryStage = venueMap.getPrimaryStage();
		if(primaryStage == null) {
			return new ArrayList<>();
		}

		List<SeatWithScore> seatsWithScores = new ArrayList<>();

		// Thu thập tất cả ghế và tính điểm
		for(Section section : venueMap.getSections()) {
			if(Objects.equals(section.getType(), SectionType.SEATED)) {
				for(Row row : section.getRows()) {
					for(Seat seat : row.getSeats()) {
						if(Objects.equals(seat.getStatus(), SeatStatus.AVAILABLE)) {
							Position seatPos = calculateAbsoluteSeatPosition(
									venueMap, section.getId(), row.getId(), seat.getId()
							);
							double score = primaryStage.calculateSeatQualityScore(seatPos);
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
	 * Tìm ghế dựa trên id
	 */
	private static Seat getSeatById(SeatContainer seatContainer, String seatId) {
		return seatContainer.getSeats().stream()
				.filter(s -> s.getId().equals(seatId))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Seat not found"));
	}
}
