package com.gyp.ticketservice.services.impl;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.gyp.common.exceptions.QRLogoImageNotFoundException;
import com.gyp.common.services.MailService;
import com.gyp.ticketservice.dtos.mail.TicketMailConfirmRequestDto;
import com.gyp.ticketservice.dtos.ticketgeneration.TicketGenerationResponseDto;
import com.gyp.ticketservice.services.PDFService;
import com.gyp.ticketservice.services.QRCodeService;
import com.gyp.ticketservice.services.TicketDeliveryService;
import com.gyp.ticketservice.services.TicketGenerationService;
import com.nimbusds.jose.util.Pair;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

@Service
@RequiredArgsConstructor
public class TicketDeliveryServiceImpl implements TicketDeliveryService {
	private final TicketGenerationService ticketGenerationService;
	private final QRCodeService qrCodeService;
	private final PDFService pdfService;
	private final MailService mailService;

	@Override
	public void sendByEmail(TicketMailConfirmRequestDto ticketMailConfirmRequestDto) throws IOException {
		if(ticketMailConfirmRequestDto.isHasQrCode()) {
			var qrCodeRes = createTicketQR(ticketMailConfirmRequestDto.getTicketId(), false);
			byte[] qrCodeBytes = qrCodeRes.getLeft();
			TicketGenerationResponseDto generationResponseDto = qrCodeRes.getRight();

			String qrCodeBase64 = Base64.getEncoder().encodeToString(qrCodeBytes);
			Map<String, Object> model = createBaseSendMail(generationResponseDto);
			model.put("qrCode", "data:image/png;base64," + qrCodeBase64);
			mailService.sendEmail(generationResponseDto.getAttendeeEmail(), "Ticket", model,
					"ticket-email-template-with-image");
		}
	}

	@Override
	public void sendByEmailWithAttachment(TicketMailConfirmRequestDto ticketMailConfirmRequestDto) {
		if(ticketMailConfirmRequestDto.isHasTickPdfAttachment()) {
			var ticketPDF = createTicketPDF(ticketMailConfirmRequestDto.getTicketId());
			byte[] ticketPDFBytes = ticketPDF.getLeft();
			TicketGenerationResponseDto generationResponseDto = ticketPDF.getRight();

			Map<String, Object> model = createBaseSendMail(generationResponseDto);
			if(ticketPDFBytes != null && ticketPDFBytes.length > 0) {
				mailService.sendEmailWithAttachment(generationResponseDto.getAttendeeEmail(),
						"Ticket", model, "ticket-email-template", ticketPDFBytes,
						"ticket-" + generationResponseDto.getTicketNumber() + ".pdf");
			} else {
				mailService.sendEmail(generationResponseDto.getAttendeeEmail(), "Ticket", model,
						"ticket-email-template-with-image");
			}
		}
	}

	@Override
	public Pair<byte[], TicketGenerationResponseDto> createTicketPDF(String id) {
		TicketGenerationResponseDto ticket = ticketGenerationService.getTicketGenerationById(id);
		if(Objects.nonNull(ticket)) {
			byte[] pdfBytes = pdfService.generateTicketPDF(ticket);
			if(pdfBytes != null && pdfBytes.length > 0) {
				return Pair.of(pdfBytes, ticket);
			}
		}
		return null;
	}

	@Override
	public Pair<byte[], TicketGenerationResponseDto> createTicketQR(String id, boolean hasLogoImage)
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

	@Override
	public Pair<byte[], TicketGenerationResponseDto> createTicketQRWithCustomImage(String id, byte[] logoBytes) {
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

	private Map<String, Object> createBaseSendMail(TicketGenerationResponseDto dto) {
		Map<String, Object> model = new HashMap<>();
		String content = """
					<p>Thank you for using our service.</p>
					<p>This is a email sent you a ticket</p>
					<p>Please check if you have any questions, feel free to contact with us.</p>
				""";
		model.put("name", dto.getAttendeeName());
		model.put("content", content);
		return model;
	}
}
