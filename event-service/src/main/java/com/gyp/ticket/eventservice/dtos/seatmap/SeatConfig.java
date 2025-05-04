package com.gyp.ticket.eventservice.dtos.seatmap;

import java.util.HashMap;
import java.util.Map;

import com.gyp.common.enums.event.VenueType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SeatConfig {
	private VenueType venueType;
	private Map<String, String> seatTypeColors = new HashMap<>();
	private SeatMap seatMap;
}
