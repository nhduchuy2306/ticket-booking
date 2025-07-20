package com.gyp.ticketservice.dtos.seatmap;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// styling properties
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VisualStyle {
	private String fillColor;
	private String strokeColor;
	private double strokeWidth;
	private double opacity;
	private String[] dashPattern; // for dashed lines
	private boolean visible;
	private int zindex; // layering
}
