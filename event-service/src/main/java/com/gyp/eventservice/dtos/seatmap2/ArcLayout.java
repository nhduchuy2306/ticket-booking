package com.gyp.eventservice.dtos.seatmap2;

/**
 * Interface cho các dạng bố trí vòng cung
 * Interface for arc section
 */
public interface ArcLayout {
	boolean isArc();

	ArcProperties getArcProperties();

	void setArcProperties(ArcProperties arcProperties);
}
