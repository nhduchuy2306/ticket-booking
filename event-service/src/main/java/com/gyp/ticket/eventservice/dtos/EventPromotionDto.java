package com.gyp.ticket.eventservice.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventPromotionDto {
	private String id;
	private String code;
	private BigDecimal discountAmount;
	private LocalDateTime validFrom;
	private LocalDateTime validTo;
	private String eventId;
}
