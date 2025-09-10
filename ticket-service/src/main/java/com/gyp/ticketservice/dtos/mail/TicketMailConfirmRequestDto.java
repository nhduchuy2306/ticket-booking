package com.gyp.ticketservice.dtos.mail;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketMailConfirmRequestDto {
	private List<String> ticketIds;
	private boolean hasQrCode;
	private boolean hasTickPdfAttachment;
}
