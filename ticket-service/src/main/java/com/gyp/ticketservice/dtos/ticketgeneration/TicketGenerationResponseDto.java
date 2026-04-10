package com.gyp.ticketservice.dtos.ticketgeneration;

import java.time.LocalDateTime;

import com.gyp.common.enums.event.TicketStatus;
import com.gyp.ticketservice.dtos.AbstractDto;
import com.gyp.ticketservice.entities.TicketEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketGenerationResponseDto extends AbstractDto {
	private String id;
	private String eventId;
	private String eventName;
	private String seatId;
	private String seatInfo;
	private String seatLabel;
	private String sectionId;
	private String rowId;
	private String ticketTypeId;
	private String ticketNumber;
	private String attendeeName;
	private String attendeeEmail;
	private LocalDateTime eventDateTime;
	private TicketStatus status;
	private TicketEntity ticketEntity;
}
