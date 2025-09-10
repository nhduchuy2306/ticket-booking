package com.gyp.ticketservice.services.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.gyp.common.exceptions.QRLogoImageNotFoundException;
import com.gyp.ticketservice.dtos.mail.TicketMailConfirmRequestDto;
import com.gyp.ticketservice.dtos.ticketgeneration.TicketGenerationResponseDto;
import com.gyp.ticketservice.services.MailService;
import com.gyp.ticketservice.services.QRCodeService;
import com.gyp.ticketservice.services.TicketDeliveryService;
import com.gyp.ticketservice.services.TicketGenerationService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

@Service
@RequiredArgsConstructor
public class TicketDeliveryServiceImpl implements TicketDeliveryService {
	private final TicketGenerationService ticketGenerationService;
	private final QRCodeService qrCodeService;
	private final MailService mailService;

	@Override
	public void sendByEmailWithAttachment(TicketMailConfirmRequestDto ticketMailConfirmRequestDto,
			List<byte[]> ticketPDFBytes, List<TicketGenerationResponseDto> ticketGenerationResponseDtoList) {
		if(ticketMailConfirmRequestDto.isHasTickPdfAttachment()) {
			Map<String, Object> model = new HashMap<>();
			String content = """
						<p>Thank you for using our service.</p>
						<p>This is a email sent you a ticket</p>
						<p>Please check if you have any questions, feel free to contact with us.</p>
					""";
			model.put("name", ticketGenerationResponseDtoList.getFirst().getAttendeeName());
			model.put("content", content);
			if(CollectionUtils.isNotEmpty(ticketPDFBytes)) {
				Map<String, Pair<byte[], String>> attachments = new HashMap<>();
				for(byte[] ticketPDFByte : ticketPDFBytes) {
					attachments.put("tickets.pdf", Pair.of(ticketPDFByte, "application/pdf"));
				}

				mailService.sendEmailWithMultipleAttachments(
						ticketGenerationResponseDtoList.getFirst().getAttendeeEmail(),
						"Ticket", model, "ticket-email-template", attachments);
			} else {
				mailService.sendEmail(ticketGenerationResponseDtoList.getFirst().getAttendeeEmail(),
						"Ticket", model, "ticket-email-template-with-image");
			}
		}
	}

	private Pair<byte[], TicketGenerationResponseDto> createTicketQR(String id, boolean hasLogoImage)
			throws IOException {
		String urlPath = "http://192.168.100.146:9002/tickets/validateticket?ticketnumber=";
		byte[] logoBytes = null;

		TicketGenerationResponseDto ticket = ticketGenerationService.getTicketGenerationById(id);

		if(hasLogoImage) {
			Resource logoResource = new ClassPathResource("static/images/myLogo.svg");
			logoBytes = FileCopyUtils.copyToByteArray(logoResource.getInputStream());
		}

		if(Objects.nonNull(ticket)) {
			String qrContent = urlPath + ticket.getTicketNumber();
			byte[] qrCodeBytes;
			if(hasLogoImage && logoBytes.length > 0) {
				qrCodeBytes = qrCodeService.generateQRCodeWithLogo(qrContent, 250, 250, logoBytes);
			} else {
				qrCodeBytes = qrCodeService.generateQRCode(qrContent, 250, 250);
			}
			if(qrCodeBytes != null && qrCodeBytes.length > 0) {
				return Pair.of(qrCodeBytes, ticket);
			}
		}
		return null;
	}

	private Pair<byte[], TicketGenerationResponseDto> createTicketQRWithCustomImage(String id, byte[] logoBytes) {
		String urlPath = "http://192.168.100.146:9002/tickets/validateticket?ticketnumber=";
		TicketGenerationResponseDto ticket = ticketGenerationService.getTicketGenerationById(id);
		if(Objects.nonNull(ticket)) {
			if(logoBytes == null || logoBytes.length == 0) {
				throw new QRLogoImageNotFoundException("Logo does not found");
			}

			String qrContent = urlPath + ticket.getTicketNumber();
			byte[] qrCodeBytes = qrCodeService.generateQRCodeWithLogo(qrContent, 250, 250, logoBytes);
			if(qrCodeBytes != null && qrCodeBytes.length > 0) {
				return Pair.of(qrCodeBytes, ticket);
			}
		}
		return null;
	}
}
