package com.gyp.eventservice.dtos.seatmap;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "@type")
@JsonSubTypes({
		@JsonSubTypes.Type(value = RectangleSeatMap.class, name = "RectangleSeatMap"),
		@JsonSubTypes.Type(value = CircleSeatMap.class, name = "CircleSeatMap"),
		@JsonSubTypes.Type(value = PointSeatMap.class, name = "PointSeatMap"),
		@JsonSubTypes.Type(value = CustomSeatMap.class, name = "CustomSeatMap")
})
public interface SeatMap {
	String getType();
}
