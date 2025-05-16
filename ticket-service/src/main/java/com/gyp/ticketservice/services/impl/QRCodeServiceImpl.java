package com.gyp.ticketservice.services.impl;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.gyp.ticketservice.services.QRCodeService;
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

	@Override
	public byte[] generateQRCodeWithLogo(String content, int width, int height, byte[] logoBytes) {
		try {
			// Generate the QR code as a BufferedImage
			QRCodeWriter qrCodeWriter = new QRCodeWriter();
			BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height);
			BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

			// Load the logo as a BufferedImage
			BufferedImage logoImage = ImageIO.read(new ByteArrayInputStream(logoBytes));

			// Calculate logo size - typically 20-25% of the QR code size for good readability
			int logoWidth = width / 5;
			int logoHeight = height / 5;

			// Scale the logo to the desired size
			BufferedImage scaledLogo = new BufferedImage(logoWidth, logoHeight, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2d = scaledLogo.createGraphics();
			g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2d.drawImage(logoImage, 0, 0, logoWidth, logoHeight, null);
			g2d.dispose();

			// Calculate the center position
			int centerX = (width - logoWidth) / 2;
			int centerY = (height - logoHeight) / 2;

			// Create a new image that combines the QR code and the logo
			BufferedImage combined = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			Graphics2D combinedGraphics = combined.createGraphics();
			combinedGraphics.drawImage(qrImage, 0, 0, null);
			combinedGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

			// Optional: Create a white background for the logo to ensure visibility
			combinedGraphics.setColor(Color.WHITE);
			combinedGraphics.fillRoundRect(centerX - 2, centerY - 2, logoWidth + 4, logoHeight + 4, 10, 10);

			// Draw the logo
			combinedGraphics.drawImage(scaledLogo, centerX, centerY, null);
			combinedGraphics.dispose();

			// Convert the combined image to byte array
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ImageIO.write(combined, "PNG", outputStream);
			return outputStream.toByteArray();
		} catch(WriterException | IOException e) {
			throw new RuntimeException("Failed to generate QR Code with logo", e);
		}
	}
}
