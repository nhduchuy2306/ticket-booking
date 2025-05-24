package com.gyp.eventservice.dtos.seatmap;

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
public class Seat extends BaseSeatMap implements Positionable {
	private Position position;
	private SeatStatus status;
	private Map<String, Boolean> attributes = new HashMap<>();
	private boolean useAbsolutePosition;
	private Position absolutePosition;
	private String ticketTypeId;

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
}
