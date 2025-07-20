package com.gyp.ticketservice.dtos.seatmap;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Lớp đại diện cho một ghế ngồi
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Seat extends BaseSeatMap implements Positionable, Styleable, Interactive {
	private Position position;
	private SeatStatus status;
	private Map<String, Boolean> attributes = new HashMap<>();
	private boolean useAbsolutePosition;
	private Position absolutePosition;
	private String ticketTypeId;
	private VisualStyle visualStyle;
	private InteractiveProperties interactiveProperties;

	public void setAbsolutePosition(Position absolutePosition) {
		this.absolutePosition = absolutePosition;
		useAbsolutePosition = true;
	}

	/**
	 * Thêm thuộc tính cho ghế
	 */
	public void addAttribute(String key, boolean value) {
		attributes.put(key, value);
	}

	/**
	 * Kiểm tra xem ghế có thuộc tính đã cho không
	 */
	public boolean hasAttribute(String key) {
		return attributes.containsKey(key) && attributes.get(key);
	}

	public VisualStyle getStatusBasedStyle() {
		if(visualStyle == null) {
			visualStyle = new VisualStyle();
		}

		switch(status) {
			case AVAILABLE:
				visualStyle.setFillColor("#00FF00"); // Green color for available seats
				break;
			case RESERVED:
				visualStyle.setFillColor("#FFFF00"); // Yellow color for reserved seats
				break;
			case SOLD:
				visualStyle.setFillColor("#FF0000"); // Red color for sold seats
				break;
			case BLOCKED:
				visualStyle.setFillColor("#0000FF"); // Blue color for blocked seats
				break;
			default:
				visualStyle.setFillColor("#FFFFFF"); // Default to white if status is unknown
				break;
		}

		return visualStyle;
	}
}
