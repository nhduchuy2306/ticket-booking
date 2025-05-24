package com.gyp.eventservice.dtos.seatmap;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Lớp đại diện cho một bàn
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Table extends BaseSeatMap implements Positionable, Dimensional, SeatContainer {
	private Position position;
	private Dimension dimensions;
	private TableShape shape;
	private int capacity;
	private List<Seat> seats = new ArrayList<>();

	@Override
	public void addSeat(Seat seat) {
		seats.add(seat);
	}

	@Override
	public void removeSeat(Seat seat) {
		seats.remove(seat);
	}

	/**
	 * Tạo ghế xung quanh bàn tròn
	 */
	public void generateSeatsForRoundTable(int count, double radius) {
		double angleStep = 360.0 / count;

		for(int i = 0; i < count; i++) {
			double angle = i * angleStep;
			double radians = Math.toRadians(angle);

			double x = radius * Math.cos(radians);
			double y = radius * Math.sin(radians);

			Seat seat = new Seat();
			seat.setName(String.valueOf(i + 1));
			seat.setPosition(new Position(x, y));
			this.addSeat(seat);
		}
	}

	/**
	 * Tạo ghế xung quanh bàn chữ nhật
	 */
	public void generateSeatsForRectangleTable(int seatsPerLongSide, int seatsPerShortSide) {
		int totalSeats = 2 * (seatsPerLongSide + seatsPerShortSide);
		double width = this.dimensions.getWidth();
		double height = this.dimensions.getHeight();

		int seatCount = 0;

		// Ghế ở cạnh trên
		for(int i = 0; i < seatsPerLongSide; i++) {
			Seat seat = new Seat();
			seat.setName(String.valueOf(++seatCount));
			double x = (width / (seatsPerLongSide + 1)) * (i + 1);
			seat.setPosition(new Position(x, 0));
			this.addSeat(seat);
		}

		// Ghế ở cạnh phải
		for(int i = 0; i < seatsPerShortSide; i++) {
			Seat seat = new Seat();
			seat.setName(String.valueOf(++seatCount));
			double y = (height / (seatsPerShortSide + 1)) * (i + 1);
			seat.setPosition(new Position(width, y));
			this.addSeat(seat);
		}

		// Ghế ở cạnh dưới
		for(int i = seatsPerLongSide - 1; i >= 0; i--) {
			Seat seat = new Seat();
			seat.setName(String.valueOf(++seatCount));
			double x = (width / (seatsPerLongSide + 1)) * (i + 1);
			seat.setPosition(new Position(x, height));
			this.addSeat(seat);
		}

		// Ghế ở cạnh trái
		for(int i = seatsPerShortSide - 1; i >= 0; i--) {
			Seat seat = new Seat();
			seat.setName(String.valueOf(++seatCount));
			double y = (height / (seatsPerShortSide + 1)) * (i + 1);
			seat.setPosition(new Position(0, y));
			this.addSeat(seat);
		}
	}
}
