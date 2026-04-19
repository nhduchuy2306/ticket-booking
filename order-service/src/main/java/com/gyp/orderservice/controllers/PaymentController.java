package com.gyp.orderservice.controllers;

import java.io.IOException;
import java.net.URI;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import com.gyp.orderservice.services.MoMoPaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(PaymentController.PAYMENT_CONTROLLER_RESOURCE_PATH)
public class PaymentController {
	public static final String PAYMENT_CONTROLLER_RESOURCE_PATH = "payments";
	private static final String CREATE_PAYMENT_ENDPOINT_PATH = "createpaymentendpoint";
	private static final String PAYMENT_INFO_PATH = "paymentinfo";
	private static final String PAYMENT_NOTIFY_PATH = "paymentnotify";
	private static final String AMOUNT_PARAM = "amount";

	private final MoMoPaymentService moMoPaymentService;

	@GetMapping(CREATE_PAYMENT_ENDPOINT_PATH)
	public ResponseEntity<?> createPaymentEndpoint(@RequestParam(AMOUNT_PARAM) Long amount,
			@RequestParam("orderId") String orderId) {
		try {
			return ResponseEntity.ok(moMoPaymentService.createMoMoPaymentEndpoint(amount, orderId));
		} catch(InvalidKeyException | NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	@GetMapping(PAYMENT_INFO_PATH)
	public ResponseEntity<?> getPaymentInfo(
			@RequestParam("partnerCode") String partnerCode,
			@RequestParam("orderId") String orderId,
			@RequestParam("requestId") String requestId,
			@RequestParam("amount") String amount,
			@RequestParam("orderInfo") String orderInfo,
			@RequestParam("orderType") String orderType,
			@RequestParam("transId") String transId,
			@RequestParam("resultCode") String resultCode,
			@RequestParam("message") String message,
			@RequestParam("payType") String payType,
			@RequestParam("responseTime") String responseTime,
			@RequestParam("extraData") String extraData,
			@RequestParam("signature") String signature) {
		try {
			String redirectUrl = moMoPaymentService.getPaymentResponseInfo(
					partnerCode, orderId, requestId, amount, orderInfo, orderType, transId, resultCode, message,
					payType, responseTime, extraData, signature);
			return ResponseEntity
					.status(302)
					.location(URI.create(redirectUrl))
					.build();
		} catch(InvalidKeyException | NoSuchAlgorithmException | IOException e) {
			throw new RuntimeException(e);
		}
	}

	@PostMapping(PAYMENT_NOTIFY_PATH)
	public ResponseEntity<?> receivePaymentNotification(@RequestBody Map<String, Object> payload) {
		// Handle the payment notification payload as needed
		log.info("Received payment notification: {}", payload);
		return ResponseEntity.ok("Notification received");
	}
}
