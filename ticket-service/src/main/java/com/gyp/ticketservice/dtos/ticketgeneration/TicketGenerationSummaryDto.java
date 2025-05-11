package com.gyp.ticketservice.dtos.ticketgeneration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketGenerationSummaryDto {
	private String attendeeName;
	private String ticketNumber;
	private String seatInfo;
}
