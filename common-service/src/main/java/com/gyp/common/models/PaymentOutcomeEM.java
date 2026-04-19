package com.gyp.common.models;

import com.gyp.common.enums.order.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentOutcomeEM {
	private String orderId;
	private String holdToken;
	private String eventId;
	private String customerEmail;
	private Double totalAmount;
	private PaymentStatus paymentStatus;
	private String idempotencyKey;
}