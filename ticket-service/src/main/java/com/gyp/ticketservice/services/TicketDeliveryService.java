package com.gyp.ticketservice.services;

import java.io.IOException;

import com.gyp.ticketservice.dtos.mail.TicketMailConfirmRequestDto;
import com.gyp.ticketservice.dtos.ticketgeneration.TicketGenerationResponseDto;
import com.nimbusds.jose.util.Pair;

public interface TicketDeliveryService {
	void sendByEmail(TicketMailConfirmRequestDto ticketMailConfirmRequestDto) throws IOException;

	void sendByEmailWithAttachment(TicketMailConfirmRequestDto ticketMailConfirmRequestDto);

	Pair<byte[], TicketGenerationResponseDto> createTicketPDF(String id);

	Pair<byte[], TicketGenerationResponseDto> createTicketQR(String id, boolean hasLogoImage) throws IOException;

	Pair<byte[], TicketGenerationResponseDto> createTicketQRWithCustomImage(String id, byte[] logoBytes);
}
