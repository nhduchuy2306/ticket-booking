package com.gyp.eventservice.dtos.templateseatmap;

import java.util.Map;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TemplateUploadDto {
	/**
	 * Type of the seat map layout. Acceptable values: Point, Rectangle, Circle, Custom
	 */
	private String seatMapType;

	/**
	 * Configuration of the stage (label, position, size, etc.)
	 */
	private StageDto stageConfig;

	/**
	 * Mapping of seat types to their color codes for visualization
	 */
	private Map<String, String> seatTypeColors;

	/**
	 * Seat layout details grouped by rows (used for Point type)
	 */
	private SeatLayoutDto seatLayout;
}
