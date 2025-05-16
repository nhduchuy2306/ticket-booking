package com.gyp.ticketservice.controllers;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import com.gyp.common.services.MailService;
import com.gyp.ticketservice.dtos.ticketgeneration.TicketGenerationResponseDto;
import com.gyp.ticketservice.dtos.ticketgeneration.TicketGenerationSummaryDto;
import com.gyp.ticketservice.services.PDFService;
import com.gyp.ticketservice.services.QRCodeService;
import com.gyp.ticketservice.services.TicketGenerationService;
import com.gyp.ticketservice.services.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(TicketController.TICKET_CONTROLLER_PATH)
public class TicketController extends AbstractController {
	public static final String TICKET_CONTROLLER_PATH = "/tickets";

	private static final String QR_CODE_PATH = "qrcode";
	private static final String TICKET_PDF_PATH = "pdf";
	private static final String VALIDATE_TICKET_PATH = "validateticket";
	private static final String SEND_MAIL_PATH = "sendmail";

	private static final String TICKET_NUMBER_PARAM = "ticketnumber";

	private final QRCodeService qrCodeService;
	private final PDFService pdfService;
	private final TicketService ticketService;
	private final TicketGenerationService ticketGenerationService;
	private final MailService mailService;

	//	@GetMapping("/{" + ID_PARAM + "}/" + QR_CODE_PATH)
	//	public ResponseEntity<byte[]> getQRCode(@PathVariable(ID_PARAM) String id) {
	//		TicketResponseDto dto = ticketService.getTicketById(id);
	//
	//		//		String qrContent = "TICKET:" + dto.getTicketNumber() +
	//		//						   ",EVENT:" + dto.getEventName() +
	//		//						   ",ATTENDEE:" + dto.getAttendeeName();
	//
	//		byte[] qrCodeImage = qrCodeService.generateQRCode("", 250, 250);
	//
	//		HttpHeaders headers = new HttpHeaders();
	//		headers.setContentType(MediaType.IMAGE_PNG);
	//		headers.setContentLength(qrCodeImage.length);
	//
	//		return new ResponseEntity<>(qrCodeImage, headers, HttpStatus.OK);
	//	}

	@GetMapping(QR_CODE_PATH)
	public ResponseEntity<byte[]> getQRCode() throws IOException {
		Resource logoResource = new ClassPathResource("static/images/myLogo.svg");
		byte[] logoBytes = FileCopyUtils.copyToByteArray(logoResource.getInputStream());

		byte[] qrCodeBytes = qrCodeService.generateQRCodeWithLogo(
				"https://yourwebsite.com/verify", 200, 200, logoBytes);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_PNG);
		headers.setContentLength(qrCodeBytes.length);

		return new ResponseEntity<>(qrCodeBytes, headers, HttpStatus.OK);
	}

	@GetMapping("/{" + ID_PARAM + "}/" + TICKET_PDF_PATH)
	public ResponseEntity<byte[]> getTicketPDF(@PathVariable(ID_PARAM) String id) {
		TicketGenerationResponseDto ticket = ticketGenerationService.getTicketGenerationById(id);

		byte[] pdfBytes = pdfService.generateTicketPDF(ticket);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		headers.setContentDispositionFormData("filename", "ticket-" + ticket.getTicketNumber() + ".pdf");
		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

		return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
	}

	@GetMapping("/" + VALIDATE_TICKET_PATH)
	public ResponseEntity<?> validateTicket(@RequestParam(TICKET_NUMBER_PARAM) String ticketNumber) {
		TicketGenerationSummaryDto ticketGenerationSummaryDto = ticketGenerationService.validateTicket(ticketNumber);
		if(ticketGenerationSummaryDto != null) {
			return ResponseEntity.ok().body(ticketGenerationSummaryDto);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}

	//	@GetMapping("/" + SEND_MAIL_PATH)
	//	public ResponseEntity<?> sendMail() {
	//		Map<String, Object> model = new HashMap<>();
	//		model.put("name", "Nguyễn Minh Duy");
	//		model.put("content", "<p>This is a <strong>complex</strong> email with HTML content and CSS styling.</p>");
	//
	//		TicketGenerationResponseDto ticket = ticketGenerationService.getTicketGenerationById("tk-gen-1");
	//
	//		byte[] pdfBytes = pdfService.generateTicketPDF(ticket);
	//
	//		mailService.sendEmailWithAttachment("abc@gmail.com", "Import", model, "ticket-email-template", pdfBytes,
	//				"ticket-" + ticket.getTicketNumber() + ".pdf");
	//		return ResponseEntity.ok("Send mail success");
	//	}

	@GetMapping("/" + SEND_MAIL_PATH)
	public ResponseEntity<?> sendMail() {
		byte[] qrCodeImage = qrCodeService.generateQRCode(
				"https://chat.deepseek.com", 250, 250);
		String qrCodeBase64 = Base64.getEncoder().encodeToString(qrCodeImage);

		Map<String, Object> model = new HashMap<>();
		model.put("name", "Nguyễn Hoang Duc Huy");
		model.put("content", "<p>This is a <strong>complex</strong> email with HTML content and CSS styling.</p>");
		model.put("qrCode", "data:image/png;base64," + qrCodeBase64);

		mailService.sendEmail("abc@gmail.com", "Import", model, "ticket-email-template-with-image");
		return ResponseEntity.ok("Send mail success");
	}
}
