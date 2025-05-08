package com.gyp.ticketservice.dtos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketDto {
	private String id;
	private String eventName;
	private String attendeeName;
	private String attendeeEmail;
	private LocalDateTime eventDateTime;
	private String ticketType;
	private String seatInfo;
	private String ticketNumber;
}
