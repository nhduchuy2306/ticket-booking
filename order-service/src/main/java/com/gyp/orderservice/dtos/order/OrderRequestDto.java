package com.gyp.orderservice.dtos.order;

import java.util.List;

import com.gyp.orderservice.dtos.orderdetail.OrderDetailRequestDto;
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
public class OrderRequestDto {
	private String eventId;
	private String customerEmail;
	private Double totalAmount;
	private List<OrderDetailRequestDto> orderDetails;
}
