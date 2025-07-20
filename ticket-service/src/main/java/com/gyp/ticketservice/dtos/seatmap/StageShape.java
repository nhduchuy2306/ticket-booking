package com.gyp.ticketservice.dtos.seatmap;

/**
 * Enum định nghĩa hình dạng của sân khấu
 */
public enum StageShape {
	RECTANGLE,     // Sân khấu chữ nhật
	CIRCULAR,      // Sân khấu tròn
	SEMICIRCLE,    // Sân khấu bán nguyệt
	THRUST,        // Sân khấu đẩy ra (3 mặt khán giả)
	ARENA,         // Sân khấu giữa (4 mặt khán giả)
	CUSTOM         // Hình dạng tùy chỉnh
}
