package com.gyp.ticketservice.services.impl;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

import com.gyp.ticketservice.dtos.ticketgeneration.TicketGenerationResponseDto;
import com.gyp.ticketservice.services.PDFService;
import com.gyp.ticketservice.services.QRCodeService;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PDFServiceImpl implements PDFService {
	private static final BaseColor BRAND = new BaseColor(18, 45, 85);
	private static final BaseColor SURFACE = new BaseColor(247, 249, 252);
	private static final BaseColor BORDER = new BaseColor(221, 228, 235);

	private final QRCodeService qrCodeService;

	@Override
	public byte[] generateTicketPDF(TicketGenerationResponseDto ticket) {
		try {
			Document document = new Document(new Rectangle(595, 842), 28, 28, 30, 28);
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			PdfWriter.getInstance(document, outputStream);
			document.open();

			document.add(buildHeader(ticket));
			document.add(Chunk.NEWLINE);
			document.add(buildDetailTable(ticket));
			document.add(Chunk.NEWLINE);
			document.add(buildQrSection(ticket));
			document.add(Chunk.NEWLINE);
			document.add(buildFooter());

			document.close();
			return outputStream.toByteArray();
		} catch(Exception e) {
			throw new RuntimeException("Failed to generate ticket PDF", e);
		}
	}

	private PdfPTable buildHeader(TicketGenerationResponseDto ticket) throws Exception {
		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(100);
		table.setWidths(new float[] {3.2f, 1.0f});

		PdfPCell titleCell = new PdfPCell();
		titleCell.setBackgroundColor(BRAND);
		titleCell.setBorder(Rectangle.NO_BORDER);
		titleCell.setPadding(16f);
		Font eventFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, BaseColor.WHITE);
		Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA, 9, new BaseColor(220, 230, 242));
		Paragraph eventTitle = new Paragraph(ticket.getEventName(), eventFont);
		Paragraph subtitle = new Paragraph("Admission Ticket", subtitleFont);
		titleCell.addElement(eventTitle);
		titleCell.addElement(subtitle);
		table.addCell(titleCell);

		PdfPCell qrCell = new PdfPCell();
		qrCell.setBackgroundColor(BRAND);
		qrCell.setBorder(Rectangle.NO_BORDER);
		qrCell.setPadding(12f);
		qrCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		String qrContent = "http://192.168.100.146:9002/tickets/validateticket?ticketnumber=" + ticket.getTicketNumber();
		byte[] qrCodeBytes = qrCodeService.generateQRCode(qrContent, 160, 160);
		Image qrCodeImage = Image.getInstance(qrCodeBytes);
		qrCodeImage.scaleToFit(86f, 86f);
		qrCodeImage.setAlignment(Element.ALIGN_CENTER);
		qrCell.addElement(qrCodeImage);
		table.addCell(qrCell);

		return table;
	}

	private PdfPTable buildDetailTable(TicketGenerationResponseDto ticket) throws DocumentException {
		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(100);
		table.setWidths(new float[] {1.15f, 2.15f});

		addDetailRow(table, "Ticket #", ticket.getTicketNumber());
		addDetailRow(table, "Attendee", ticket.getAttendeeName());
		addDetailRow(table, "Date & Time", ticket.getEventDateTime() == null
				? "-"
				: ticket.getEventDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
		addDetailRow(table, "Seat", resolveSeatDisplay(ticket));
		addDetailRow(table, "Seat Key", ticket.getSeatId());

		return table;
	}

	private PdfPTable buildQrSection(TicketGenerationResponseDto ticket) {
		PdfPTable table = new PdfPTable(1);
		table.setWidthPercentage(100);
		PdfPCell cell = new PdfPCell();
		cell.setBorderColor(BORDER);
		cell.setBackgroundColor(SURFACE);
		cell.setPadding(14f);

		Font noteTitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, BRAND);
		Font noteBodyFont = FontFactory.getFont(FontFactory.HELVETICA, 9, BaseColor.DARK_GRAY);
		Paragraph noteTitle = new Paragraph("Gate Check", noteTitleFont);
		Paragraph noteBody = new Paragraph();
		noteBody.setFont(noteBodyFont);
		noteBody.add("Show the QR code above at the entrance. ");
		noteBody.add("This ticket is valid for one person and one scan only.");
		cell.addElement(noteTitle);
		cell.addElement(noteBody);
		cell.addElement(Chunk.NEWLINE);
		cell.setPaddingBottom(18f);
		table.addCell(cell);

		return table;
	}

	private PdfPTable buildFooter() {
		PdfPTable table = new PdfPTable(1);
		table.setWidthPercentage(100);
		PdfPCell cell = new PdfPCell(new Phrase("Please arrive early and keep this ticket ready for inspection."));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setPaddingTop(4f);
		cell.setPhrase(new Phrase("Please arrive early and keep this ticket ready for inspection.",
				FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 9, new BaseColor(95, 103, 115))));
		table.addCell(cell);
		return table;
	}

	private void addDetailRow(PdfPTable table, String label, String value) {
		Font labelFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BRAND);
		Font valueFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);

		PdfPCell labelCell = new PdfPCell(new Phrase(label, labelFont));
		labelCell.setBackgroundColor(SURFACE);
		labelCell.setBorderColor(BORDER);
		labelCell.setPadding(10f);

		PdfPCell valueCell = new PdfPCell(new Phrase(value == null || value.isBlank() ? "-" : value, valueFont));
		valueCell.setBorderColor(BORDER);
		valueCell.setPadding(10f);

		table.addCell(labelCell);
		table.addCell(valueCell);
	}

	private String resolveSeatDisplay(TicketGenerationResponseDto ticket) {
		String seatLabel = ticket.getSeatLabel();
		String seatInfo = ticket.getSeatInfo();
		if(seatLabel != null && !seatLabel.isBlank()) {
			StringBuilder builder = new StringBuilder(seatLabel);
			if(ticket.getSectionId() != null || ticket.getRowId() != null) {
				builder.append(" (");
				if(ticket.getSectionId() != null) {
					builder.append(ticket.getSectionId());
				}
				if(ticket.getRowId() != null) {
					if(ticket.getSectionId() != null) {
						builder.append(" / ");
					}
					builder.append(ticket.getRowId());
				}
				builder.append(")");
			}
			return builder.toString();
		}
		return seatInfo != null && !seatInfo.isBlank() ? seatInfo : ticket.getSeatId();
	}
}
