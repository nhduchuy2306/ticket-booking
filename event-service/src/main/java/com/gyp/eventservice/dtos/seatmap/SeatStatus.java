package com.gyp.eventservice.dtos.seatmap;

/**
 * Enum cho trạng thái ghế
 */
public enum SeatStatus {
	AVAILABLE,  // Ghế có sẵn
	RESERVED,   // Ghế đã đặt tạm thời
	SOLD,       // Ghế đã bán
	BLOCKED,    // Ghế bị khóa (không thể đặt)
	DISABLED    // Ghế bị vô hiệu hóa (không tồn tại)
}
