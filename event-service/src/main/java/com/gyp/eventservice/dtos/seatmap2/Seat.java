package com.gyp.eventservice.dtos.seatmap2;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Lớp đại diện cho một ghế ngồi
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Seat extends BaseEntity implements Positionable {
	private Position position;
	private SeatStatus status;
	private Map<String, Boolean> attributes;
	private boolean useAbsolutePosition;
	private Position absolutePosition;

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
		return attributes.containsKey(key) && this.attributes.get(key);
	}
}
