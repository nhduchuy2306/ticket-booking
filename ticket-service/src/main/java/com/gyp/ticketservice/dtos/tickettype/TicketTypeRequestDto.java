package com.gyp.ticketservice.dtos.tickettype;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketTypeRequestDto {
	private String eventId;
	private String name;
	private String description;
	private double price;
	private Integer quantityAvailable;
	private LocalDateTime saleStartDate;
	private LocalDateTime saleEndDate;
}
