package com.gyp.ticketservice.services.criteria;

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
public class TicketSearchCriteria {
	private String eventId;
	private String ticketId;
	private String organizationId;
	private String status;
	private String ticketType;
	private String seatNumber;
	private String orderBy;
}
