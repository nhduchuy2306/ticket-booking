package com.gyp.eventservice.dtos.seatmap;

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
public class RenderingProperties {
	private boolean cached;
	private double pixelRatio;
	private String[] filters;
	private boolean perfectDrawEnabled;
}
