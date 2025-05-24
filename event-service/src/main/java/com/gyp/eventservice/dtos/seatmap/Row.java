package com.gyp.eventservice.dtos.seatmap;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Lớp đại diện cho một hàng ghế
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Row extends BaseSeatMap implements Positionable, SeatContainer, ArcLayout {
	private Position position;
	private List<Seat> seats = new ArrayList<>();
	private boolean isArc;
	private ArcProperties arcProperties;

	@Override
	public void addSeat(Seat seat) {
		seats.add(seat);
	}

	@Override
	public void removeSeat(Seat seat) {
		seats.remove(seat);
	}

	@Override
	public int getCapacity() {
		return seats.size();
	}

	@Override
	public void setArcProperties(ArcProperties arcProperties) {
		this.arcProperties = arcProperties;
		isArc = (arcProperties != null);
	}

	/**
	 * Tạo các ghế theo hàng thẳng với khoảng cách đều
	 */
	public void generateLinearSeats(int seatPerRow, double startX, double spacing) {
		for(int i = 0; i < seatPerRow; i++) {
			Seat seat = new Seat();
			seat.setName(String.valueOf(i + 1));
			seat.setPosition(new Position(startX + i * spacing, 0));
			addSeat(seat);
		}
	}

	/**
	 * Tạo các ghế theo hàng vòng cung
	 */
	public void generateArcSeats(int count, ArcProperties arcProps) {
		if(arcProps == null) {
			throw new IllegalArgumentException("Arc properties cannot be null");
		}

		double angleRange = arcProps.getEndAngle() - arcProps.getStartAngle();
		double angleStep = angleRange / (count - 1);

		for(int i = 0; i < count; i++) {
			double angle = arcProps.getStartAngle() + i * angleStep;
			Position seatPosition = arcProps.calculatePointOnArc(angle);

			Seat seat = new Seat();
			seat.setName(String.valueOf(i + 1));
			seat.setPosition(seatPosition);
			addSeat(seat);
		}
	}
}
