package com.gyp.ticketservice.controllers;

import java.io.IOException;
import java.util.Objects;

import com.gyp.common.services.MailService;
import com.gyp.ticketservice.dtos.mail.TicketMailConfirmRequestDto;
import com.gyp.ticketservice.services.QRCodeService;
import com.gyp.ticketservice.services.TicketDeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(TicketDeliveryController.TICKET_DELIVERY_CONTROLLER_PATH)
public class TicketDeliveryController extends AbstractController {
	public static final String TICKET_DELIVERY_CONTROLLER_PATH = "/ticketdeliveries";
	private static final String QR_CODE_PATH = "qrcode";
	private static final String QR_CODE_WITH_LOGO_PATH = "qrcodewithlogo";
	private static final String TICKET_PDF_PATH = "pdf";
	private static final String SEND_MAIL_PATH = "sendmail";

	private final QRCodeService qrCodeService;
	private final TicketDeliveryService ticketDeliveryService;
	private final MailService mailService;

	@GetMapping("/{" + ID_PARAM + "}/" + QR_CODE_PATH)
	public ResponseEntity<byte[]> getQRCode(@PathVariable(ID_PARAM) String id) throws IOException {
		var qrCodeRes = ticketDeliveryService.createTicketQR(id, false);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_PNG);
		headers.setContentLength(qrCodeRes.getLeft().length);

		return new ResponseEntity<>(qrCodeRes.getLeft(), headers, HttpStatus.OK);
	}

	@GetMapping("/{" + ID_PARAM + "}/" + QR_CODE_WITH_LOGO_PATH)
	public ResponseEntity<?> getQRCodeWithLogo(@PathVariable(ID_PARAM) String id) throws IOException {
		var qrCodeRes = ticketDeliveryService.createTicketQR(id, true);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_PNG);
		headers.setContentLength(qrCodeRes.getLeft().length);

		return new ResponseEntity<>(qrCodeRes.getLeft(), headers, HttpStatus.OK);
	}

	@GetMapping("/{" + ID_PARAM + "}/" + TICKET_PDF_PATH)
	public ResponseEntity<?> getTicketPDF(@PathVariable(ID_PARAM) String id) {
		var response = ticketDeliveryService.createTicketPDF(id);
		if(Objects.isNull(response)) {
			return ResponseEntity.badRequest().build();
		}
		var pdfBytes = response.getLeft();
		var ticket = response.getRight();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		headers.setContentDispositionFormData("filename", "ticket-" + ticket.getTicketNumber() + ".pdf");
		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

		return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
	}

	@PostMapping()
	public ResponseEntity<?> sendMailWithAttachment(@RequestBody TicketMailConfirmRequestDto dto) {
		ticketDeliveryService.sendByEmailWithAttachment(dto);
		return ResponseEntity.ok("Send Mail successfully");
	}
}
