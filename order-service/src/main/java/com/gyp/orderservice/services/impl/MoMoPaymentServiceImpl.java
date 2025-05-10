package com.gyp.orderservice.services.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.gyp.orderservice.clients.momoservice.MomoServiceClient;
import com.gyp.orderservice.dtos.payment.PaymentResponseDto;
import com.gyp.orderservice.services.MoMoPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MoMoPaymentServiceImpl implements MoMoPaymentService {
	@Value("${momo.partner.code}")
	private String partnerCode;
	@Value("${momo.access.key}")
	private String accessKey;
	@Value("${momo.secret.key}")
	private String secretKey;
	@Value("${momo.return.url}")
	private String returnUrl;
	@Value("${momo.notify.url}")
	private String notifyUrl;

	private final MomoServiceClient momoServiceClient;

	@Override
	public Object createMoMoPaymentEndpoint(Long amount)
			throws InvalidKeyException, NoSuchAlgorithmException, IOException {
		String orderId = UUID.randomUUID().toString();
		String orderInfo = "PAY WITH MOMO";
		String requestId = UUID.randomUUID().toString();
		String requestType = "captureWallet";
		String extraData = "";
		String lang = "en";
		String partnerName = "Shop";
		String storeId = "MoMoStore";

		String requestRawData = "accessKey" + "=" + accessKey + "&"
								+ "amount" + "=" + amount + "&"
								+ "extraData" + "=" + extraData + "&"
								+ "ipnUrl" + "=" + notifyUrl + "&"
								+ "orderId" + "=" + orderId + "&"
								+ "orderInfo" + "=" + orderInfo + "&"
								+ "partnerCode" + "=" + partnerCode + "&"
								+ "redirectUrl" + "=" + returnUrl + "&"
								+ "requestId" + "=" + requestId + "&"
								+ "requestType" + "=" + requestType;

		String signature = signHmacSHA256(requestRawData, secretKey);

		Map<String, String> paymentBody = new HashMap<>();
		paymentBody.put("partnerCode", partnerCode);
		paymentBody.put("partnerName", partnerName);
		paymentBody.put("storeId", storeId);
		paymentBody.put("requestId", requestId);
		paymentBody.put("amount", String.valueOf(amount));
		paymentBody.put("orderId", orderId);
		paymentBody.put("orderInfo", orderInfo);
		paymentBody.put("redirectUrl", returnUrl);
		paymentBody.put("ipnUrl", notifyUrl);
		paymentBody.put("lang", lang);
		paymentBody.put("extraData", extraData);
		paymentBody.put("requestType", requestType);
		paymentBody.put("signature", signature);

		return momoServiceClient.processPayment(paymentBody);
	}

	@Override
	public PaymentResponseDto compareSignature(String partnerCode, String orderId, String requestId,
			String amount, String orderInfo, String orderType, String transId, String resultCode, String message,
			String payType, String responseTime, String extraData, String signature)
			throws InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {
		String requestRawData = "accessKey" + "=" + accessKey + "&"
								+ "amount" + "=" + amount + "&"
								+ "extraData" + "=" + extraData + "&"
								+ "message" + "=" + message + "&"
								+ "orderId" + "=" + orderId + "&"
								+ "orderInfo" + "=" + orderInfo + "&"
								+ "orderType" + "=" + orderType + "&"
								+ "partnerCode" + "=" + partnerCode + "&"
								+ "payType" + "=" + payType + "&"
								+ "requestId" + "=" + requestId + "&"
								+ "responseTime" + "=" + responseTime + "&"
								+ "resultCode" + "=" + resultCode + "&"
								+ "transId" + "=" + transId;

		String signRequest = signHmacSHA256(requestRawData, secretKey);

		if(!signRequest.equals(signature)) {
			return PaymentResponseDto.builder()
					.message("INVALID SIGNATURE")
					.status(resultCode)
					.build();
		}
		return PaymentResponseDto.builder()
				.message(message)
				.status(resultCode)
				.build();
	}

	private String signHmacSHA256(String data, String secretKey)
			throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
		SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
		Mac mac = Mac.getInstance("HmacSHA256");
		mac.init(secretKeySpec);
		byte[] rawHmac = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
		return toHexString(rawHmac);
	}

	private String toHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder(bytes.length * 2);
		Formatter formatter = new Formatter(sb);
		for(byte b : bytes) {
			formatter.format("%02x", b);
		}
		formatter.close();
		return sb.toString();
	}
}
