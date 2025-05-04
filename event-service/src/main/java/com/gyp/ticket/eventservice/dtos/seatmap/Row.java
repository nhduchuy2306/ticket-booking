package com.gyp.ticket.eventservice.dtos.seatmap;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Row {
	private String rowName;
	private List<Seat> seats;
	private Integer seatCount;
	private Integer price;
}
