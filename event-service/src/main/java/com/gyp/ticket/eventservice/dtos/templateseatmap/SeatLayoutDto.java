package com.gyp.ticket.eventservice.dtos.templateseatmap;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SeatLayoutDto {
	/**
	 * List of seat rows
	 */
	private List<RowDto> rows;
}
