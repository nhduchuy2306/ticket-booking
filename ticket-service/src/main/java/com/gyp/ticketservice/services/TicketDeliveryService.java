package com.gyp.ticketservice.services;

import java.util.List;

import com.gyp.ticketservice.dtos.mail.TicketMailConfirmRequestDto;
import com.gyp.ticketservice.dtos.ticketgeneration.TicketGenerationResponseDto;

public interface TicketDeliveryService {
	void sendByEmailWithAttachment(TicketMailConfirmRequestDto ticketMailConfirmRequestDto, List<byte[]> ticketPdfs,
			List<TicketGenerationResponseDto> ticketGenerationResponseDtoList);
}
