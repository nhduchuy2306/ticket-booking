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
	private String orderId;
	private String idempotencyKey;
	private String description;
	private EventStatus status;
	private String seatId;
	private String seatLabel;
	private String sectionId;
	private String rowId;
	private String ticketTypeId;
	private List<String> seatIds;
	private List<String> seatLabels;
	private List<String> sectionIds;
	private List<String> rowIds;
	private List<String> ticketTypeIds;
	private String logoUrl;
	private String venueAddress;
	private String customerEmail;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private LocalDateTime doorOpenTime;
	private LocalDateTime doorCloseTime;
}
