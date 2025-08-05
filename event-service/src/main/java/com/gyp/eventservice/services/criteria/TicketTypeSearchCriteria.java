package com.gyp.eventservice.services.criteria;

import com.gyp.common.enums.event.TicketStatus;
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
public class TicketTypeSearchCriteria {
	private String id;
	private TicketStatus status;
	private Double price;
	private Integer minQuantity;
	private String organizationId;
}
