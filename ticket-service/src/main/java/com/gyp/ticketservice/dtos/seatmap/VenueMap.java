package com.gyp.ticketservice.dtos.seatmap;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class VenueMap extends BaseSeatMap implements Dimensional {
	private Dimension dimensions;
	private SeatConfig seatConfig;
	private StageConfig stageConfig;

	public void calculateSeatQualityScores() {
		if(stageConfig == null) {
			return;
		}

		for(Section section : seatConfig.getSections()) {
			if(Objects.equals(section.getType(), SectionType.SEATED)) {
				for(Row row : section.getRows()) {
					for(Seat seat : row.getSeats()) {
						Position seatAbsolutePos = SeatMapUtil.calculateAbsoluteSeatPosition(
								this, section.getId(), row.getId(), seat.getId()
						);
						double qualityScore = stageConfig.calculateSeatQualityScore(seatAbsolutePos);
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
						double qualityScore = stageConfig.calculateSeatQualityScore(seatAbsolutePos);
						seat.addAttribute("qualityScore", qualityScore > 0.5);
						seat.addAttribute("premiumSeat", qualityScore > 0.8);
					}
				}
			}
		}
	}
}
