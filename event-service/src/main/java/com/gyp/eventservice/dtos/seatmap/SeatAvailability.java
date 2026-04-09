package com.gyp.eventservice.dtos.seatmap;

import java.time.LocalDateTime;

import com.gyp.eventservice.entities.SeatInventoryStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Runtime snapshot of a seat.
 * Layout data comes from the seat-map blob; status and hold metadata come from
 * the inventory tables so the UI can render the actual booking state.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatAvailability {
	private String seatId;
	private String seatKey;
	private String seatLabel;
	private String sectionId;
	private String rowId;
	private String ticketTypeId;
	private Double price;
	private SeatInventoryStatus status;
	private boolean available;
	private String holdToken;
	private LocalDateTime holdExpiresAt;
}
