package com.gyp.ticketservice.dtos.ticket;

import java.time.LocalDateTime;

import com.gyp.common.enums.event.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketRequestDto {
	private String ticketCode;
	private String eventId;
	private String eventName;
	private LocalDateTime eventDateTime;
	private LocalDateTime reservedDateTime;
	private String ticketTypeId;
	private String seatInfo;
	private TicketStatus status;
	private String attendeeName;
	private String attendeeEmail;
	private String qrCodeUrl;
	private String pdfUrl;
}
