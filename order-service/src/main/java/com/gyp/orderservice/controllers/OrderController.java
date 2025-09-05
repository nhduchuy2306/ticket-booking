package com.gyp.orderservice.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(OrderController.ORDER_CONTROLLER_RESOURCE_PATH)
public class OrderController {
	public static final String ORDER_CONTROLLER_RESOURCE_PATH = "/orders";
}
