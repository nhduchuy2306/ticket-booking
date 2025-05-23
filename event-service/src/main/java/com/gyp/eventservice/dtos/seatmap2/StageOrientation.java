package com.gyp.eventservice.dtos.seatmap2;

import lombok.Getter;

/**
 * Enum định nghĩa hướng của sân khấu
 */
@Getter
public enum StageOrientation {
	NORTH(0),      // Hướng Bắc (0 độ)
	NORTHEAST(45), // Hướng Đông Bắc (45 độ)
	EAST(90),      // Hướng Đông (90 độ)
	SOUTHEAST(135),// Hướng Đông Nam (135 độ)
	SOUTH(180),    // Hướng Nam (180 độ)
	SOUTHWEST(225),// Hướng Tây Nam (225 độ)
	WEST(270),     // Hướng Tây (270 độ)
	NORTHWEST(315);// Hướng Tây Bắc (315 độ)

	private final double angle;

	StageOrientation(double angle) {
		this.angle = angle;
	}
}
