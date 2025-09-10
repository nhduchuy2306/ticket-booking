package com.gyp.ticketservice.dtos.seatmap;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InteractiveProperties {
	private boolean draggable;
	private boolean selectable;
	private boolean hoverAble;
	private String cursor;
	private Map<String, Object> eventHandlers;
}
