package com.gyp.eventservice.dtos.seatmap;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RenderLayer {
	BACKGROUND(0),
	STAGE(1),
	SECTIONS(2),
	SEATS(3),
	LABELS(4),
	INTERACTIVE_OVERLAY(5);

	private final int zIndex;
}
