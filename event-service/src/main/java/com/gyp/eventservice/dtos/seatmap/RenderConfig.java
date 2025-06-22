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
public class RenderConfig {
	private boolean useCache = true;
	private int cacheThreshold = 100; // cache if more than 100 seats
	private boolean enableHitTesting = true;
	private double scaleThreshold = 0.5; // hide details when zoomed out
}
