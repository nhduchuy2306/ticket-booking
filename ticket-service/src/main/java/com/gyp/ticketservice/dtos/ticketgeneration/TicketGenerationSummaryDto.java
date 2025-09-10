package com.gyp.ticketservice.dtos.ticketgeneration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketGenerationSummaryDto {
	private String attendeeName;
	private String ticketNumber;
	private String seatInfo;
	private String seatId;
}
