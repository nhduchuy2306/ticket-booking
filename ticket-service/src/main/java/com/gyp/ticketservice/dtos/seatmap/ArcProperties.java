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
public class ArcProperties {
	private double centerX;
	private double centerY;
	private double radius;
	private double startAngle;
	private double endAngle;
	private double thickness;

	public Position calculatePointOnArc(double angle) {
		double radians = Math.toRadians(angle);
		double x = centerX + radius * Math.cos(radians);
		double y = centerY + radius * Math.sin(radians);
		return Position.builder().x(x).y(y).build();
	}
}
