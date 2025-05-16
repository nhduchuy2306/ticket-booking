package com.gyp.ticketservice.services;

public interface QRCodeService {
	byte[] generateQRCode(String content, int width, int height);
	byte[] generateQRCodeWithLogo(String content, int width, int height, byte[] logo);
}
