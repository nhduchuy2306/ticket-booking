package com.gyp.eventservice.dtos.eventpromotion;

import java.time.LocalDateTime;

import com.gyp.eventservice.dtos.AbstractDto;
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
public class EventPromotionResponseDto extends AbstractDto {
	private String id;
	private String code;
	private Double discountAmount;
	private LocalDateTime validFrom;
	private LocalDateTime validTo;

	// Calculated properties
	private boolean isActive;
	private boolean isExpired;
	private String organizationId;
}
