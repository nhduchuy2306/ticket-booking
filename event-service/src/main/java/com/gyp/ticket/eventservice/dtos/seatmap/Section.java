package com.gyp.ticket.eventservice.dtos.seatmap;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Section {
	private String name;
	private String seatType;
	private List<Row> rows;
}
