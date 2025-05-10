package com.gyp.orderservice.controllers;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import jakarta.ws.rs.QueryParam;

import com.gyp.orderservice.services.MoMoPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(PaymentController.PAYMENT_CONTROLLER_RESOURCE_PATH)
public class PaymentController {
	public static final String PAYMENT_CONTROLLER_RESOURCE_PATH = "payments";
	private static final String CREATE_PAYMENT_ENDPOINT_PATH = "createpaymentendpoint";
	private static final String AMOUNT_PARAM = "amount";

	private final MoMoPaymentService moMoPaymentService;

	@GetMapping(CREATE_PAYMENT_ENDPOINT_PATH)
	public ResponseEntity<?> createPaymentEndpoint(@QueryParam(AMOUNT_PARAM) Long amount) {
		try {
			return ResponseEntity.ok(moMoPaymentService.createMoMoPaymentEndpoint(amount));
		} catch(InvalidKeyException | NoSuchAlgorithmException | IOException e) {
			throw new RuntimeException(e);
		}
	}
}
