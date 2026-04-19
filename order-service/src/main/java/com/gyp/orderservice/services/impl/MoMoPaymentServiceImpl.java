package com.gyp.orderservice.services.impl;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.gyp.common.enums.order.OrderStatus;
import com.gyp.common.enums.order.PaymentStatus;
import com.gyp.common.models.PaymentOutcomeEM;
import com.gyp.orderservice.clients.MomoServiceClient;
import com.gyp.orderservice.dtos.payment.MomoProperties;
import com.gyp.orderservice.dtos.payment.PaymentResponseDto;
import com.gyp.orderservice.entities.OrderEntity;
import com.gyp.orderservice.messages.producers.OrderPaymentEventProducer;
import com.gyp.orderservice.repositories.OrderRepository;
import com.gyp.orderservice.services.MoMoPaymentService;
import com.gyp.orderservice.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MoMoPaymentServiceImpl implements MoMoPaymentService {
	private final MomoProperties momoProperties;
	private final MomoServiceClient momoServiceClient;
	private final OrderService orderService;
	private final OrderRepository orderRepository;
	private final OrderPaymentEventProducer orderPaymentEventProducer;

	@Override
	public Object createMoMoPaymentEndpoint(Long amount, String orderId)
			throws InvalidKeyException, NoSuchAlgorithmException {
		String orderInfo = "PAY WITH MOMO";
		String requestId = UUID.randomUUID().toString();
		String requestType = "captureWallet";
		String extraData = "";
		String lang = "en";
		String partnerName = "Shop";
		String storeId = "MoMoStore";

		String requestRawData = "accessKey" + "=" + momoProperties.getAccessKey() + "&"
				+ "amount" + "=" + amount + "&"
				+ "extraData" + "=" + extraData + "&"
				+ "ipnUrl" + "=" + momoProperties.getNotifyUrl() + "&"
				+ "orderId" + "=" + orderId + "&"
				+ "orderInfo" + "=" + orderInfo + "&"
				+ "partnerCode" + "=" + momoProperties.getPartnerCode() + "&"
				+ "redirectUrl" + "=" + momoProperties.getReturnUrl() + "&"
				+ "requestId" + "=" + requestId + "&"
				+ "requestType" + "=" + requestType;

		String signature = signHmacSHA256(requestRawData, momoProperties.getSecretKey());

		Map<String, String> paymentBody = new HashMap<>();
		paymentBody.put("partnerCode", momoProperties.getPartnerCode());
		paymentBody.put("partnerName", partnerName);
		paymentBody.put("storeId", storeId);
		paymentBody.put("requestId", requestId);
		paymentBody.put("amount", String.valueOf(amount));
		paymentBody.put("orderId", orderId);
		paymentBody.put("orderInfo", orderInfo);
		paymentBody.put("redirectUrl", momoProperties.getReturnUrl());
		paymentBody.put("ipnUrl", momoProperties.getNotifyUrl());
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
			throws InvalidKeyException, NoSuchAlgorithmException {
		String requestRawData = "accessKey" + "=" + momoProperties.getAccessKey() + "&"
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

		String signRequest = signHmacSHA256(requestRawData, momoProperties.getSecretKey());

		if(!signRequest.equals(signature)) {
			return PaymentResponseDto.builder()
					.message("INVALID SIGNATURE")
					.status(resultCode)
					.build();
		}
		return PaymentResponseDto.builder()
				.message(message)
				.status(resultCode)
				.orderId(orderId)
				.build();
	}

	@Override
	public String getPaymentResponseInfo(String partnerCode, String orderId, String requestId, String amount,
			String orderInfo, String orderType, String transId, String resultCode, String message, String payType,
			String responseTime, String extraData, String signature)
			throws NoSuchAlgorithmException, InvalidKeyException {
		String postFixUrl = "?orderId=%s&message=%s";
		PaymentResponseDto paymentResponse = compareSignature(partnerCode, orderId, requestId, amount,
				orderInfo, orderType, transId, resultCode, message, payType, responseTime, extraData, signature);
		OrderEntity orderEntity = orderRepository.findById(orderId)
				.orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + orderId));
		if("0".equals(paymentResponse.getStatus())) {
			orderService.updateOrderStatus(orderId, OrderStatus.DONE);
			orderPaymentEventProducer.sendPaymentOutcome(PaymentOutcomeEM.builder()
					.orderId(orderId)
					.holdToken(orderEntity.getHoldToken())
					.eventId(orderEntity.getEventId())
					.customerEmail(orderEntity.getCustomerEmail())
					.totalAmount(orderEntity.getTotalAmount())
					.paymentStatus(PaymentStatus.SUCCESS)
					.idempotencyKey(orderId)
					.build());
			return String.format(momoProperties.getFrontendSuccessUrl() + postFixUrl, orderId,
					paymentResponse.getMessage());
		}
		orderService.updateOrderStatus(orderId, OrderStatus.CANCELLED);
		orderPaymentEventProducer.sendPaymentOutcome(PaymentOutcomeEM.builder()
				.orderId(orderId)
				.holdToken(orderEntity.getHoldToken())
				.eventId(orderEntity.getEventId())
				.customerEmail(orderEntity.getCustomerEmail())
				.totalAmount(orderEntity.getTotalAmount())
				.paymentStatus(PaymentStatus.FAILED)
				.idempotencyKey(orderId)
				.build());
		return String.format(momoProperties.getFrontendFailureUrl() + postFixUrl, orderId,
				paymentResponse.getMessage());
	}

	private String signHmacSHA256(String data, String secretKey) throws NoSuchAlgorithmException, InvalidKeyException {
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
