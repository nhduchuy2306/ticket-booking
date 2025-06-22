package com.gyp.eventservice.dtos.seatmap;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// For animations:
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnimationConfig {
	private double duration;
	private String easing;
	private Position targetPosition;
	private Dimension targetDimensions;
	private Map<String, Object> targetProperties;
}
