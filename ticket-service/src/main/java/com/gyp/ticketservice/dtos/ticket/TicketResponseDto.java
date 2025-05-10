package com.gyp.ticketservice.dtos.ticket;

import java.time.LocalDateTime;

import com.gyp.ticketservice.dtos.AbstractDto;
import com.gyp.ticketservice.dtos.tickettype.TicketTypeSummaryDto;
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
	private String ticketTypeId;
	private LocalDateTime eventDateTime;
	private LocalDateTime reservedDateTime;
	private TicketTypeSummaryDto ticketTypeSummaryDto;
}
