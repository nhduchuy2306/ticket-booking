package com.gyp.orderservice.services;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import com.gyp.orderservice.dtos.payment.PaymentResponseDto;

public interface MoMoPaymentService {
	Object createMoMoPaymentEndpoint(Long amount) throws InvalidKeyException, NoSuchAlgorithmException, IOException;

	PaymentResponseDto compareSignature(String partnerCode, String orderId, String requestId, String amount,
			String orderInfo, String orderType, String transId, String resultCode, String message, String payType,
			String responseTime, String extraData, String signature)
			throws InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException;
}
