package com.gyp.eventservice.dtos.seatmap;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Row {
	private String rowName;
	private Integer seatCount;
	private double price;
	private List<Seat> seats;
}
