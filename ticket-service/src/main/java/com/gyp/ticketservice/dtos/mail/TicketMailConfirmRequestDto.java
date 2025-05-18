package com.gyp.ticketservice.dtos.mail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketMailConfirmRequestDto {
	private String ticketId;
	private boolean hasQrCode;
	private boolean hasTickPdfAttachment;
}
