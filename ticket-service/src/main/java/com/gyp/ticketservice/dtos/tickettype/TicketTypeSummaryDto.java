package com.gyp.ticketservice.dtos.tickettype;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketTypeSummaryDto {
	private String name;
	private String description;
}
