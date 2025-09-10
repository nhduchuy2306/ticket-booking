package com.gyp.orderservice.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(OrderDetailController.ORDER_DETAIL_CONTROLLER_PATH)
public class OrderDetailController {
	public static final String ORDER_DETAIL_CONTROLLER_PATH = "order-details";
}
