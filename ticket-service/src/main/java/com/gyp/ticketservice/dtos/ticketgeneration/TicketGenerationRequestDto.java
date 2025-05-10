package com.gyp.ticketservice.dtos.ticketgeneration;

import java.time.LocalDateTime;

import com.gyp.common.enums.event.TicketStatus;
import com.gyp.ticketservice.entities.TicketEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketGenerationRequestDto {
	private String eventId;
	private String eventName;
	private String seatInfo;
	private String ticketNumber;
	private String attendeeName;
	private String attendeeEmail;
	private LocalDateTime eventDateTime;
	private TicketStatus status;
	private TicketEntity ticketEntity;
}
