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

/**
 * Seat layout for one venue map.
 * This is the structural blueprint for sections, rows, tables, and seat ids.
 * The booking flow converts it into SeatEntity rows per event.
 */
@Getter
@Setter
@NoArgsConstructor
public class SeatConfig {
	private List<String> seatTypes = new ArrayList<>();
	private List<Section> sections = new ArrayList<>();

	/**
	 * Adds a new section to the venue layout.
	 */
	public void addSection(Section section) {
		sections.add(section);
	}

	/**
	 * Removes a section from the layout editor snapshot.
	 */
	public void removeSection(Section section) {
		sections.remove(section);
	}

	/**
	 * Flattens the nested layout into a stream of seats for scoring, export, and
	 * inventory initialization.
	 */
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

	/**
	 * Applies a status update to a set of seat ids and returns the affected seat
	 * keys used by downstream ticket generation.
	 */
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

	/**
	 * Marks seats as sold in the serialized seat-map snapshot.
	 */
	public Pair<SeatConfig, List<String>> soldSeats(List<String> seatIds) {
		return updateSeatStatus(seatIds, SeatStatus.SOLD);
	}

	/**
	 * Restores seats back to available in the serialized seat-map snapshot.
	 */
	public Pair<SeatConfig, List<String>> availableSeats(List<String> seatIds) {
		return updateSeatStatus(seatIds, SeatStatus.AVAILABLE);
	}

	/**
	 * Marks seats as temporarily reserved in the serialized seat-map snapshot.
	 */
	public Pair<SeatConfig, List<String>> reservedSeats(List<String> seatIds) {
		return updateSeatStatus(seatIds, SeatStatus.RESERVED);
	}
}
