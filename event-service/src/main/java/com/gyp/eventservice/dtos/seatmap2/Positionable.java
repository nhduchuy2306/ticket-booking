package com.gyp.eventservice.dtos.seatmap2;

/**
 * Interface cho các thành phần có vị trí trong sơ đồ
 */
public interface Positionable {
	Position getPosition();

	void setPosition(Position position);
}
