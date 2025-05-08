package com.gyp.ticketservice.dtos.tickettype;

import java.time.LocalDateTime;

import com.gyp.common.enums.event.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketTypeResponseDto {
	private String name;
	private String description;
	private double price;
	private Integer quantityAvailable;
	private TicketStatus status;
	private LocalDateTime saleStartDate;
	private LocalDateTime saleEndDate;
}
