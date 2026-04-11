package com.gyp.eventservice.dtos.seatmap;

import com.gyp.common.enums.event.SeatInventoryStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents an individual seat in a row of a section.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Seat extends BaseSeatMap implements Positionable {
	private Position position;
	private SeatInventoryStatus status;
}
