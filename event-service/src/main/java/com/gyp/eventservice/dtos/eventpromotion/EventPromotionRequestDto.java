package com.gyp.eventservice.dtos.eventpromotion;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventPromotionRequestDto {
	private String code;
	private double discountAmount;
	private LocalDateTime validFrom;
	private LocalDateTime validTo;
	private String eventId;
}
