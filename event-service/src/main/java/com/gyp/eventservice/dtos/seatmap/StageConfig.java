package com.gyp.eventservice.dtos.seatmap;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StageConfig extends BaseSeatMap implements Positionable, Dimensional {
	private Position position;
	private String description;
	private Dimension dimensions;
	private String svgPath;
	private String borderRadius;
}