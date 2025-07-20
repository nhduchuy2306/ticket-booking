package com.gyp.ticketservice.dtos.seatmap;

import java.util.List;

/**
 * Interface cho các thành phần có thể chứa chỗ ngồi
 */
public interface SeatContainer {
	List<Seat> getSeats();

	void addSeat(Seat seat);

	void removeSeat(Seat seat);

	int getCapacity();
}
