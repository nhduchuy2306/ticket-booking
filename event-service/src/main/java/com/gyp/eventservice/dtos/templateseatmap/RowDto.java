package com.gyp.eventservice.dtos.templateseatmap;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RowDto {
	/**
	 * Name or label of the row (e.g. A, B, C)
	 */
	private String rowName;

	/**
	 * Number of seats in this row
	 */
	private Integer seatCount;

	/**
	 * Default price for seats in this row (can be overridden per seat)
	 */
	private Double defaultPrice;

	/**
	 * List of individual seats
	 */
	private List<SeatDto> seats;

}
