package com.gyp.eventservice.dtos.seatmap;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request payload for reserve/confirm/release seat operations.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatHoldRequestDto {

	private String eventId;
	private String holdToken;
	private String userId;
	private List<String> seatIds = new ArrayList<>();
	private List<String> seatKeys = new ArrayList<>();
}
