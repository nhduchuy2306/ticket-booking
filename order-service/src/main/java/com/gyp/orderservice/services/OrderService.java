package com.gyp.orderservice.services;

import com.gyp.common.enums.order.OrderStatus;
import com.gyp.orderservice.dtos.order.OrderRequestDto;
import com.gyp.orderservice.dtos.order.OrderResponseDto;

public interface OrderService {
	OrderResponseDto createOrder(OrderRequestDto orderRequestDto);

	void updateOrderStatus(String orderId, OrderStatus orderStatus);
}
