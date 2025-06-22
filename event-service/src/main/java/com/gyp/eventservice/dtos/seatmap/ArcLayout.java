package com.gyp.eventservice.dtos.seatmap;

/**
 * Interface cho các dạng bố trí vòng cung
 * Interface for arc section
 */
public interface ArcLayout {
	Boolean getIsArc();

	void setIsArc(Boolean isArc);

	ArcProperties getArcProperties();

	void setArcProperties(ArcProperties arcProperties);
}
