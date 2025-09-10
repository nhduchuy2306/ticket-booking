package com.gyp.orderservice.services;

import com.gyp.orderservice.dtos.enums.PaymentTypeEnum;

public interface OrderPaymentService {
	Object createPaymentEndpoint(PaymentTypeEnum paymentType, Long amount, String orderId) throws Exception;
}
