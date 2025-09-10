package com.gyp.ticketservice.dtos.seatmap;

import java.util.List;

public interface SeatContainer {
	List<Seat> getSeats();

	void addSeat(Seat seat);

	void removeSeat(Seat seat);

	int getCapacity();
}
