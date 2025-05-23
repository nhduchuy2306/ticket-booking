package com.gyp.eventservice.dtos.seatmap2;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SeatWithScore {
	private Seat seat;
	private double score;
}
