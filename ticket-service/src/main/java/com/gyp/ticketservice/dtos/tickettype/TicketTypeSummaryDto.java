package com.gyp.ticketservice.dtos.tickettype;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketTypeSummaryDto {
	private String name;
	private String description;
}
