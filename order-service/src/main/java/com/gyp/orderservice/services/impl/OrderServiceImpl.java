package com.gyp.orderservice.services.impl;

import java.util.List;

import com.gyp.common.enums.order.OrderStatus;
import com.gyp.common.exceptions.ResourceNotFoundException;
import com.gyp.orderservice.dtos.order.OrderRequestDto;
import com.gyp.orderservice.dtos.order.OrderResponseDto;
import com.gyp.orderservice.dtos.orderdetail.OrderDetailRequestDto;
import com.gyp.orderservice.entities.OrderEntity;
import com.gyp.orderservice.mappers.OrderMapper;
import com.gyp.orderservice.repositories.OrderRepository;
import com.gyp.orderservice.services.OrderDetailService;
import com.gyp.orderservice.services.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
	private final OrderDetailService orderDetailService;
	private final OrderRepository orderRepository;
	private final OrderMapper orderMapper;

	@Transactional(rollbackFor = Exception.class)
	@Override
	public OrderResponseDto createOrder(OrderRequestDto orderRequestDto) {
		log.info("Creating order: {}", orderRequestDto);
		OrderEntity orderEntity = orderMapper.toEntity(orderRequestDto);
		orderEntity.setStatus(OrderStatus.PENDING);
		OrderEntity savedOrder = orderRepository.save(orderEntity);
		log.info("Order created with ID: {}", savedOrder.getId());

		// create order details
		List<OrderDetailRequestDto> orderDetails = orderRequestDto.getOrderDetails();
		if(orderDetails != null) {
			for(OrderDetailRequestDto detailRequestDto : orderDetails) {
				detailRequestDto.setOrderId(savedOrder.getId());
				orderDetailService.createOrderDetail(detailRequestDto);
			}
		}
		return orderMapper.toResponseDto(savedOrder);
	}

	@Override
	public void updateOrderStatus(String orderId, OrderStatus orderStatus) {
		log.info("Updating order status. Order ID: {}, New Status: {}", orderId, orderStatus);
		OrderEntity orderEntity = orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));
		orderEntity.setStatus(orderStatus);
		orderRepository.save(orderEntity);
		log.info("Order status updated successfully for Order ID: {}", orderId);
	}
}
