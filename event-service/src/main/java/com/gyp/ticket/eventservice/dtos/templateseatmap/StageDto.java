package com.gyp.ticket.eventservice.dtos.templateseatmap;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StageDto {
	/**
	 * Label shown on the stage
	 */
	private String label;

	/**
	 * X-coordinate of the stage's position
	 */
	private Integer stageX;

	/**
	 * Y-coordinate of the stage's position
	 */
	private Integer stageY;

	/**
	 * Width of the stage in pixels or logical units
	 */
	private Integer stageWidth;

	/**
	 * Height of the stage
	 */
	private Integer stageHeight;

	/**
	 * Shape of the stage: RECTANGLE, CIRCLE, CUSTOM
	 */
	private String shape;

	/**
	 * Orientation of the stage: NORTH, SOUTH, EAST, WEST
	 */
	private String orientation;

	/**
	 * Optional SVG path if shape is CUSTOM
	 */
	private String svgPath;
}
