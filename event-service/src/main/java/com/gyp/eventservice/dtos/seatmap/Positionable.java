package com.gyp.eventservice.dtos.seatmap;

/**
 * Interface cho các thành phần có vị trí trong sơ đồ
 */
public interface Positionable {
	Position getPosition();

	void setPosition(Position position);
}
