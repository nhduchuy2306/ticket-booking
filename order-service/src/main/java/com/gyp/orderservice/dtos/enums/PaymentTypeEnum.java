package com.gyp.orderservice.dtos.enums;

import lombok.Getter;

@Getter
public enum PaymentTypeEnum {
	MOMO("MoMo"),
	ZALO_PAY("ZaloPay"),
	VN_PAY("VNPAY");

	private final String displayName;

	PaymentTypeEnum(String displayName) {
		this.displayName = displayName;
	}
}
