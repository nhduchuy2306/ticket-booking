package com.gyp.eventservice.dtos.seatmap;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Seat layout for one venue map.
 * This is the structural blueprint for sections, rows, tables, and seat ids.
 * The booking flow converts it into SeatInventoryEntity rows per event.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatConfig {
	private List<Section> sections = new ArrayList<>();
}
