package com.gyp.orderservice.dtos.orderdetail;

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
public class OrderDetailRequestDto {
	private String seatId;
	private Integer quantity;
	private Double price;
	private String orderId;
}
