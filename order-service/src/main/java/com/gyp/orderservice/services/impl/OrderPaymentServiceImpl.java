package com.gyp.orderservice.services.impl;

import com.gyp.orderservice.dtos.enums.PaymentTypeEnum;
import com.gyp.orderservice.services.MoMoPaymentService;
import com.gyp.orderservice.services.OrderPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderPaymentServiceImpl implements OrderPaymentService {
	private final MoMoPaymentService moMoPaymentService;

	@Override
	public Object createPaymentEndpoint(PaymentTypeEnum paymentType, Long amount, String orderId) throws Exception {
		return switch(paymentType) {
			case PaymentTypeEnum.MOMO -> moMoPaymentService.createMoMoPaymentEndpoint(amount, orderId);
			case PaymentTypeEnum.ZALO_PAY ->
					throw new UnsupportedOperationException("ZaloPay integration is not implemented yet.");
			case PaymentTypeEnum.VN_PAY ->
					throw new UnsupportedOperationException("VNPay integration is not implemented yet.");
		};
	}
}
