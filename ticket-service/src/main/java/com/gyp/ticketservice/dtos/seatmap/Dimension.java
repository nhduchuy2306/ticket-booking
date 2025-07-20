package com.gyp.ticketservice.dtos.seatmap;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class for dimension
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Dimension {
	private double width;
	private double height;
}
