package com.gyp.ticketservice.dtos.seatmap;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
	private String[] dashPattern;
	private boolean visible;
	private int zindex;
}
