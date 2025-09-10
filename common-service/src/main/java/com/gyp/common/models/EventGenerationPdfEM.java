package com.gyp.common.models;

import java.time.LocalDateTime;
import java.util.List;

import com.gyp.common.enums.event.EventStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventGenerationPdfEM {
	private String id;
	private String name;
	private String description;
	private EventStatus status;
	private String seatId;
	private List<String> seatIds;
	private String logoUrl;
	private String venueAddress;
	private String customerEmail;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private LocalDateTime doorOpenTime;
	private LocalDateTime doorCloseTime;
}
