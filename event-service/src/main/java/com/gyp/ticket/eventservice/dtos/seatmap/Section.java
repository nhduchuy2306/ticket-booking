package com.gyp.ticket.eventservice.dtos.seatmap;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Section {
	private String name;
	private String seatType;
	private List<Row> rows;
}
