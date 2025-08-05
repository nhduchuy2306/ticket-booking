package com.gyp.eventservice.dtos.eventpromotion;

import java.time.LocalDateTime;

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
public class EventPromotionRequestDto {
	private String code;
	private double discountAmount;
	private LocalDateTime validFrom;
	private LocalDateTime validTo;
	private String eventId;
	private String organizationId;
}
