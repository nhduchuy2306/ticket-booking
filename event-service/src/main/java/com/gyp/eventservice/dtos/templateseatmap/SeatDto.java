package com.gyp.eventservice.dtos.templateseatmap;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SeatDto {
	/**
	 * Unique ID of the seat
	 */
	private String seatId;

	/**
	 * X-coordinate of the seat on the canvas
	 */
	private int x;

	/**
	 * Y-coordinate of the seat on the canvas
	 */
	private int y;

	/**
	 * Ticket type assigned to this seat (must match existing TicketType)
	 */
	private String ticketType;

	/**
	 * Price for the seat (overrides row default if provided)
	 */
	private double price;
}
