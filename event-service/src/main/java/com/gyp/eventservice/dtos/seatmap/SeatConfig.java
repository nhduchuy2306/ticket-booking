package com.gyp.eventservice.dtos.seatmap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.tuple.Pair;

@Getter
@Setter
@NoArgsConstructor
public class SeatConfig {
	private List<String> seatTypes = new ArrayList<>();
	private List<Section> sections = new ArrayList<>();

	public void addSection(Section section) {
		sections.add(section);
	}

	public void removeSection(Section section) {
		sections.remove(section);
	}

	@JsonIgnore
	public Stream<Seat> getAllSeatsStream() {
		return sections.stream()
				.flatMap(section -> {
					if(SectionType.SEATED.equals(section.getType())) {
						return section.getRows().stream()
								.flatMap(row -> row.getSeats().stream());
					} else if(SectionType.TABLE.equals(section.getType())) {
						return section.getTables().stream()
								.flatMap(table -> table.getSeats().stream());
					} else {
						return Stream.empty();
					}
				});
	}

	public Pair<SeatConfig, List<String>> updateSeatStatus(List<String> seatIds, SeatStatus newStatus) {
		Set<String> seatIdSet = new HashSet<>(seatIds);
		List<String> ticketSeatIds = new ArrayList<>();

		sections.forEach(section -> {
			if(SectionType.SEATED.equals(section.getType())) {
				section.getRows().forEach(row -> {
					row.getSeats().stream()
							.filter(seat -> seatIdSet.contains(seat.getId()))
							.forEach(seat -> {
								seat.setStatus(newStatus);
								ticketSeatIds.add(section.getId() + "-" + row.getId() + "-" + seat.getId());
							});
				});
			} else if(SectionType.TABLE.equals(section.getType())) {
				section.getTables().forEach(table -> {
					table.getSeats().stream()
							.filter(seat -> seatIdSet.contains(seat.getId()))
							.forEach(seat -> {
								seat.setStatus(newStatus);
								ticketSeatIds.add(section.getId() + "-" + table.getId() + "-" + seat.getId());
							});
				});
			}
		});

		return Pair.of(this, ticketSeatIds);
	}

	public Pair<SeatConfig, List<String>> soldSeats(List<String> seatIds) {
		return updateSeatStatus(seatIds, SeatStatus.SOLD);
	}

	public Pair<SeatConfig, List<String>> availableSeats(List<String> seatIds) {
		return updateSeatStatus(seatIds, SeatStatus.AVAILABLE);
	}

	public Pair<SeatConfig, List<String>> reservedSeats(List<String> seatIds) {
		return updateSeatStatus(seatIds, SeatStatus.RESERVED);
	}
}
