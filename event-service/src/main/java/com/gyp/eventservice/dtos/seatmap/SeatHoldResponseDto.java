package com.gyp.eventservice.dtos.seatmap;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Response payload for seat hold lifecycle operations.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatHoldResponseDto {
	private String eventId;
	private String holdToken;
	private LocalDateTime holdExpiresAt;
	private List<String> seatIds = new ArrayList<>();
	private List<SeatAvailability> seats = new ArrayList<>();
}
