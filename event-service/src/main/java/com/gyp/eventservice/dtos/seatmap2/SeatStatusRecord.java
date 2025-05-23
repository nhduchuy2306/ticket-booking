package com.gyp.eventservice.dtos.seatmap2;

import java.time.LocalDateTime;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Lớp đại diện cho trạng thái của một ghế trong một sự kiện
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SeatStatusRecord {
	private String eventId;
	private String seatId;
	private SeatStatus status;
	private LocalDateTime reservationExpiry;
	private String ticketId;

	/**
	 * Kiểm tra xem đặt chỗ đã hết hạn chưa
	 */
	public boolean isReservationExpired() {
		return Objects.equals(status, SeatStatus.RESERVED) &&
				reservationExpiry != null &&
				LocalDateTime.now().isAfter(reservationExpiry);
	}
}
