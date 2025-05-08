package com.gyp.ticketservice.controllers;

import com.gyp.ticketservice.dtos.TicketDto;
import com.gyp.ticketservice.services.PDFService;
import com.gyp.ticketservice.services.QRCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(TicketController.TICKET_CONTROLLER_PATH)
public class TicketController extends AbstractController {
	public static final String TICKET_CONTROLLER_PATH = "/tickets";

	private static final String QR_CODE_PATH = "/qrcode";
	private static final String TICKET_PDF_PATH = "/pdf";

	private final QRCodeService qrCodeService;
	private final PDFService pdfService;

	@GetMapping(ID_PARAM + QR_CODE_PATH)
	public ResponseEntity<byte[]> getQRCode(@PathVariable String id) {
		// In a real application, you would fetch the ticket from a database
		TicketDto ticket = getDummyTicket(id);

		String qrContent = "TICKET:" + ticket.getTicketNumber() +
						   ",EVENT:" + ticket.getEventName() +
						   ",ATTENDEE:" + ticket.getAttendeeName();

		byte[] qrCodeImage = qrCodeService.generateQRCode(qrContent, 250, 250);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_PNG);
		headers.setContentLength(qrCodeImage.length);

		return new ResponseEntity<>(qrCodeImage, headers, HttpStatus.OK);
	}

	@GetMapping(ID_PARAM + TICKET_PDF_PATH)
	public ResponseEntity<byte[]> getTicketPDF(@PathVariable String id) {
		// In a real application, you would fetch the ticket from a database
		TicketDto ticket = getDummyTicket(id);

		byte[] pdfBytes = pdfService.generateTicketPDF(ticket);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		headers.setContentDispositionFormData("filename", "ticket-" + ticket.getTicketNumber() + ".pdf");
		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

		return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
	}

	private TicketDto getDummyTicket(String id) {
		TicketDto ticket = new TicketDto();
		ticket.setId(id);
		ticket.setTicketNumber("TKT-" + id + "-" + System.currentTimeMillis());
		ticket.setEventName("Spring Conference 2025");
		ticket.setAttendeeName("John Doe");
		ticket.setAttendeeEmail("john.doe@example.com");
		ticket.setEventDateTime(java.time.LocalDateTime.now().plusDays(30));
		ticket.setTicketType("VIP");
		ticket.setSeatInfo("Section A, Row 1, Seat 5");
		return ticket;
	}
}
