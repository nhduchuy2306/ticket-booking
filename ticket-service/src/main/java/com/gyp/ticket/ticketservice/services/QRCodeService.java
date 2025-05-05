package com.gyp.ticket.ticketservice.services;

public interface QRCodeService {
	byte[] generateQRCode(String content, int width, int height);
}
