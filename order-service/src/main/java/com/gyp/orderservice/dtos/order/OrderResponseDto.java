package com.gyp.orderservice.dtos.order;

import com.gyp.common.dtos.AbstractDto;
import com.gyp.common.enums.order.OrderStatus;
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
public class OrderResponseDto extends AbstractDto {
	private String id;
	private String eventId;
	private String customerEmail;
	private OrderStatus status;
	private Double totalAmount;
}
