package com.gyp.ticketservice.dtos.seatmap;

import lombok.Getter;

/**
 * Enum định nghĩa hướng của sân khấu
 */
@Getter
public enum StageOrientation {
	NORTH(0),
	NORTHEAST(45),
	EAST(90),
	SOUTHEAST(135),
	SOUTH(180),
	SOUTHWEST(225),
	WEST(270),
	NORTHWEST(315);

	private final double angle;

	StageOrientation(double angle) {
		this.angle = angle;
	}
}
