package com.gyp.ticketservice.services.impl;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

import com.gyp.ticketservice.dtos.TicketDto;
import com.gyp.ticketservice.services.PDFService;
import com.gyp.ticketservice.services.QRCodeService;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PDFServiceImpl implements PDFService {

	private final QRCodeService qrCodeService;

	@Override
	public byte[] generateTicketPDF(TicketDto ticket) {
		try {
			Document document = new Document();
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			PdfWriter.getInstance(document, outputStream);

			document.open();

			// Add title
			Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, BaseColor.BLACK);
			Paragraph title = new Paragraph(ticket.getEventName() + " - TICKET", titleFont);
			title.setAlignment(Element.ALIGN_CENTER);
			document.add(title);
			document.add(new Paragraph(" ")); // Add some space

			// Add ticket info
			document.add(new Paragraph("Ticket #: " + ticket.getTicketNumber()));
			document.add(new Paragraph("Attendee: " + ticket.getAttendeeName()));
			document.add(new Paragraph("Date & Time: " +
									   ticket.getEventDateTime()
											   .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));
			document.add(new Paragraph("Type: " + ticket.getTicketType()));

			if(ticket.getSeatInfo() != null && !ticket.getSeatInfo().isEmpty()) {
				document.add(new Paragraph("Seat: " + ticket.getSeatInfo()));
			}

			document.add(new Paragraph(" "));

			// Generate and add QR code
			String qrContent = "TICKET:" + ticket.getTicketNumber() +
							   ",EVENT:" + ticket.getEventName() +
							   ",ATTENDEE:" + ticket.getAttendeeName();

			byte[] qrCodeBytes = qrCodeService.generateQRCode(qrContent, 200, 200);
			Image qrCodeImage = Image.getInstance(qrCodeBytes);
			qrCodeImage.setAlignment(Element.ALIGN_CENTER);
			document.add(qrCodeImage);

			// Add footer
			document.add(new Paragraph(" "));
			Paragraph footer = new Paragraph(
					"Please present this ticket at the entrance. This ticket is valid for one person only.");
			footer.setAlignment(Element.ALIGN_CENTER);
			document.add(footer);

			document.close();

			return outputStream.toByteArray();
		} catch(Exception e) {
			throw new RuntimeException("Failed to generate ticket PDF", e);
		}
	}
}
