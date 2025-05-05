package com.gyp.ticket.eventservice.dtos.seatmap;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * For the Coordinates x,y it will take from top left corner
 */
@Data
@NoArgsConstructor
public class StageConfig {
	private String label;
	private Integer stageX;
	private Integer stageY;
	private Integer stageWidth;
	private Integer stageHeight;
	@JsonProperty("shape")
	private StageShape shape;
	@JsonProperty("orientation")
	private StageOrientation orientation;
	private String svgPath; // In case Shape = custom
}
