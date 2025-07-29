package com.gyp.ticketservice.dtos.ticket;

import java.time.LocalDateTime;

import com.gyp.common.enums.event.TicketStatus;
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
	private String ticketCode;
	private String eventId;
	private String eventName;
	private String ticketTypeId;
	private LocalDateTime eventDateTime;
	private LocalDateTime reservedDateTime;
	private String seatInfo;
	private TicketStatus status;
	private String attendeeName;
	private String attendeeEmail;
	private String qrCodeUrl;
	private String pdfUrl;
	private TicketTypeSummaryDto ticketTypeSummaryDto;
}
