package com.gyp.ticketservice.dtos.ticket;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketRequestDto {
	private String eventId;
	private String eventName;
	private String ticketTypeId;
	private LocalDateTime eventDateTime;
	private LocalDateTime reservedDateTime;
}
