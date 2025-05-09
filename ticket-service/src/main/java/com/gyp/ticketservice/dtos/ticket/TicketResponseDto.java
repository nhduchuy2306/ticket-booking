package com.gyp.ticketservice.dtos.ticket;

import java.time.LocalDateTime;

import com.gyp.ticketservice.dtos.AbstractDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TicketResponseDto extends AbstractDto {
	private String id;
	private String eventId;
	private String eventName;
	private String seatInfo;
	private String ticketTypeId;
	private String ticketNumber;
	private String attendeeName;
	private String attendeeEmail;
	private LocalDateTime eventDateTime;
	private String status;
	private LocalDateTime reservedDateTime;
}
