package com.gyp.orderservice.services;

import com.gyp.orderservice.dtos.orderdetail.OrderDetailRequestDto;
import com.gyp.orderservice.dtos.orderdetail.OrderDetailResponseDto;

public interface OrderDetailService {
	OrderDetailResponseDto createOrderDetail(OrderDetailRequestDto orderDetailRequestDto);
}
