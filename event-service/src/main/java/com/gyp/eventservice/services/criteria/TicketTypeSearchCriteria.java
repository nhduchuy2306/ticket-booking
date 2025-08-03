package com.gyp.eventservice.services.criteria;

import java.time.LocalDateTime;

import com.gyp.common.enums.event.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketTypeSearchCriteria {
	private String id;
	private TicketStatus status;
	private Double price;
	private Integer minQuantity;
	private String organizationId;
}
