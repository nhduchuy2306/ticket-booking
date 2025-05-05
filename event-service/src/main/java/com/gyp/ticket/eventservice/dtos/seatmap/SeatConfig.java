package com.gyp.ticket.eventservice.dtos.seatmap;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SeatConfig {
	private String venueType;
	private Map<String, String> seatTypeColors = new HashMap<>();
	private SeatMap seatMap;
}
