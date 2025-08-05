package com.gyp.eventservice.dtos.tickettype;

import java.time.LocalDateTime;

import com.gyp.common.enums.event.TicketStatus;
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
public class TicketTypeDto extends AbstractDto {
	private String id;
	private String name;
	private String description;
	private double price;
	private Integer quantityAvailable;
	private TicketStatus status;
	private LocalDateTime saleStartDate;
	private LocalDateTime saleEndDate;

	// Calculated fields
	private Integer soldTickets;
	private boolean isSaleActive;
	private boolean isSoldOut;
	private String organizationId;
}
