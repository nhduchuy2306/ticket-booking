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
public class RenderConfig {
	private boolean useCache = true;
	private int cacheThreshold = 100;
	private boolean enableHitTesting = true;
	private double scaleThreshold = 0.5;
}
