package com.gyp.eventservice.dtos.seatmap;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Lớp biểu diễn vị trí tọa độ
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Position {
	private double x;
	private double y;

	/**
	 * Tính toán vị trí tuyệt đối dựa trên vị trí cha
	 */
	public Position calculateAbsolutePosition(Position parentPosition) {
		return new Position(parentPosition.getX() + x, parentPosition.getY() + y);
	}
}
