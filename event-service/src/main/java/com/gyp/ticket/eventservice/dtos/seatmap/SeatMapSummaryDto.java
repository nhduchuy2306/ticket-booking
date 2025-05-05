package com.gyp.ticket.eventservice.dtos.seatmap;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatMapSummaryDto {
	private String id;
	private String name;
	private String venueType;
}
