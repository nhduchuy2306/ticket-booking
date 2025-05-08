package com.gyp.eventservice.dtos.tickettype;

import com.gyp.common.enums.event.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketTypeSummaryDto {
	private String id;
	private String name;
	private double price;
	private TicketStatus status;
}
