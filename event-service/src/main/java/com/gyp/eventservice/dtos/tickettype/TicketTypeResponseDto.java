package com.gyp.eventservice.dtos.tickettype;

import java.time.LocalDateTime;

import com.gyp.common.enums.event.TicketStatus;
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
public class TicketTypeResponseDto extends AbstractDto {
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
	private String eventId;
}
