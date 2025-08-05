package com.gyp.eventservice.dtos.templateseatmap;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SeatLayoutDto {
	/**
	 * List of seat rows
	 */
	private List<RowDto> rows;
}
