package com.gyp.ticketservice.dtos.mail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketMailConfirmRequestDto {
	private String ticketId;
	private boolean hasQrCode;
	private boolean hasTickPdfAttachment;
}
