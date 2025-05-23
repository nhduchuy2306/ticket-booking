package com.gyp.eventservice.dtos.seatmap2;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Properties for Arc
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArcProperties {
	private double centerX;
	private double centerY;
	private double radius;
	private double startAngle;
	private double endAngle;

	/**
	 * Tính toán vị trí một điểm trên vòng cung dựa vào góc
	 * Calculate 1 point in arc base on angle
	 */
	public Position calculatePointOnArc(double angle) {
		double radians = Math.toRadians(angle);
		double x = centerX + radius * Math.cos(radians);
		double y = centerY + radius * Math.sin(radians);
		return new Position(x, y);
	}
}
