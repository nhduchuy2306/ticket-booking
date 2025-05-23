package com.gyp.eventservice.dtos.seatmap2;

import java.util.List;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Lớp đại diện cho sơ đồ địa điểm
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VenueMap extends BaseEntity implements Dimensional {
	private Dimension dimensions;
	private List<Section> sections;
	private List<Stage> stages;

	public void addSection(Section section) {
		sections.add(section);
	}

	public void removeSection(Section section) {
		sections.remove(section);
	}

	public void addStage(Stage stage) {
		stages.add(stage);
	}

	public void removeStage(Stage stage) {
		stages.remove(stage);
	}

	/**
	 * Lấy sân khấu chính (active)
	 */
	public Stage getPrimaryStage() {
		return stages.stream()
				.filter(Stage::isActive)
				.findFirst()
				.orElse(null);
	}

	/**
	 * Tính điểm chất lượng cho tất cả ghế dựa trên sân khấu chính
	 */
	public void calculateSeatQualityScores() {
		Stage primaryStage = getPrimaryStage();
		if(primaryStage == null) {
			return;
		}

		for(Section section : sections) {
			if(Objects.equals(section.getType(), SectionType.SEATED)) {
				for(Row row : section.getRows()) {
					for(Seat seat : row.getSeats()) {
						Position seatAbsolutePos = SeatMapUtil.calculateAbsoluteSeatPosition(
								this, section.getId(), row.getId(), seat.getId()
						);
						double qualityScore = primaryStage.calculateSeatQualityScore(seatAbsolutePos);
						seat.addAttribute("qualityScore", qualityScore > 0.5);
						seat.addAttribute("premiumSeat", qualityScore > 0.8);
					}
				}
			} else if(Objects.equals(section.getType(), SectionType.TABLE)) {
				for(Table table : section.getTables()) {
					for(Seat seat : table.getSeats()) {
						Position seatAbsolutePos = SeatMapUtil.calculateAbsoluteSeatPositionInTable(
								this, section.getId(), table.getId(), seat.getId()
						);
						double qualityScore = primaryStage.calculateSeatQualityScore(seatAbsolutePos);
						seat.addAttribute("qualityScore", qualityScore > 0.5);
						seat.addAttribute("premiumSeat", qualityScore > 0.8);
					}
				}
			}
		}
	}
}
