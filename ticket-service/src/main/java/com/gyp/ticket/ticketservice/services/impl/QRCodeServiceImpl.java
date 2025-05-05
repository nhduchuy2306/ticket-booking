package com.gyp.ticket.ticketservice.services.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.gyp.ticket.ticketservice.services.QRCodeService;
import org.springframework.stereotype.Service;

@Service
public class QRCodeServiceImpl implements QRCodeService {
	@Override
	public byte[] generateQRCode(String content, int width, int height) {
		try {
			QRCodeWriter qrCodeWriter = new QRCodeWriter();
			BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height);

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);

			return outputStream.toByteArray();
		} catch(WriterException | IOException e) {
			throw new RuntimeException("Failed to generate QR Code", e);
		}
	}
}
