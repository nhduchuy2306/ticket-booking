package com.gyp.eventservice.dtos.seatmap;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Presented as a horizontal grouping of seats within a section, with shared styling and spacing.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Row extends BaseSeatMap implements Positionable, SeatContainer {
	private Position position;
	private String borderRadius;
	private Double seatSpacing;
	private List<Seat> seats = new ArrayList<>();

	@Override
	public void addSeat(Seat seat) {
		seats.add(seat);
	}

	@Override
	public void removeSeat(Seat seat) {
		seats.remove(seat);
	}

	@JsonIgnore
	@Override
	public int getCapacity() {
		return seats.size();
	}
}
