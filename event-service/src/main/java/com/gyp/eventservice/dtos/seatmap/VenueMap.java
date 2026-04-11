package com.gyp.eventservice.dtos.seatmap;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class define for Venue Map
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VenueMap extends BaseSeatMap implements Dimensional {
	private Dimension dimensions;
	private SeatConfig seatConfig;
	private StageConfig stageConfig;
}
