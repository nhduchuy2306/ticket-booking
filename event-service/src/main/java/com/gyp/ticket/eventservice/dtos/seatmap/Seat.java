package com.gyp.ticket.eventservice.dtos.seatmap;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Seat {
	private String seatId;
	private int x;
	private int y;
	private String ticketTypeId;
	private double price;
}
