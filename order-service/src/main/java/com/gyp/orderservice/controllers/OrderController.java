package com.gyp.orderservice.controllers;

import com.gyp.orderservice.dtos.order.OrderRequestDto;
import com.gyp.orderservice.services.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(OrderController.ORDER_CONTROLLER_RESOURCE_PATH)
public class OrderController {
	public static final String ORDER_CONTROLLER_RESOURCE_PATH = "/orders";
	private static final String CREATE_ORDER_ENDPOINT_PATH = "/createorder";

	private final OrderService orderService;

	@PostMapping(CREATE_ORDER_ENDPOINT_PATH)
	public ResponseEntity<?> createOrder(@RequestBody OrderRequestDto orderRequestDto) {
		return ResponseEntity.ok(orderService.createOrder(orderRequestDto));
	}
}
