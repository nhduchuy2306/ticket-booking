package com.gyp.eventservice.dtos.eventpromotion;

import java.time.LocalDateTime;

import com.gyp.eventservice.dtos.AbstractDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
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
