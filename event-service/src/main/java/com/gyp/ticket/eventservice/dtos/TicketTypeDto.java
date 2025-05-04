package com.gyp.ticket.eventservice.dtos;

import java.time.LocalDateTime;

import com.gyp.common.enums.event.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketTypeDto {
	private String id;
	private String name;
	private String description;
	private double price;
	private Integer quantityAvailable;
	private TicketStatus status;
	private LocalDateTime saleStartDate;
	private LocalDateTime saleEndDate;
	private String eventId;
}
