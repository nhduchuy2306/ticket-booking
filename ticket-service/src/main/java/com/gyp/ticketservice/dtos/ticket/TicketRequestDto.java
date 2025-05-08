package com.gyp.ticketservice.dtos.ticket;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketRequestDto {
	private String eventName;
	private String attendeeName;
	private String attendeeEmail;
	private LocalDateTime eventDateTime;
	private String ticketType;
	private String seatInfo;
	private String ticketNumber;
}
