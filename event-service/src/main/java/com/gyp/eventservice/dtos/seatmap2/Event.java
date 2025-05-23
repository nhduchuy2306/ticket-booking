package com.gyp.eventservice.dtos.seatmap2;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Lớp đại diện cho một sự kiện
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Event extends BaseEntity {
	private String venueId;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private String venueMapId;
	private List<SeatStatusRecord> seatStatusRecords;

	public void addSeatStatusRecord(SeatStatusRecord record) {
		seatStatusRecords.add(record);
	}

	/**
	 * Lấy trạng thái của ghế trong sự kiện
	 */
	public SeatStatus getSeatStatus(String seatId) {
		return seatStatusRecords.stream()
				.filter(record -> record.getSeatId().equals(seatId))
				.findFirst()
				.map(SeatStatusRecord::getStatus)
				.orElse(SeatStatus.AVAILABLE);
	}

	/**
	 * Cập nhật trạng thái ghế
	 */
	public void updateSeatStatus(String seatId, SeatStatus status) {
		seatStatusRecords.stream()
				.filter(record -> record.getSeatId().equals(seatId))
				.findFirst()
				.ifPresentOrElse(
						record -> record.setStatus(status),
						() -> {
							SeatStatusRecord newRecord = new SeatStatusRecord();
							newRecord.setEventId(this.getId());
							newRecord.setSeatId(seatId);
							newRecord.setStatus(status);
							addSeatStatusRecord(newRecord);
						}
				);
	}
}
