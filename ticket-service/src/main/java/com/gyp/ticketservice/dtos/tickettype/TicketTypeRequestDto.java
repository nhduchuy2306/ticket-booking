package com.gyp.ticketservice.dtos.tickettype;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
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
