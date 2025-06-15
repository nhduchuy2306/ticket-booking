package com.gyp.eventservice.dtos.tickettype;

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
public class TicketTypeRequestDto {
	private String name;
	private String description;
	private double price;
	private String color;
	private Integer quantityAvailable;
	private TicketStatus status;
	private LocalDateTime saleStartDate;
	private LocalDateTime saleEndDate;
	private String eventId;
}
