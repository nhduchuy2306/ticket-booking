package com.gyp.orderservice.services.impl;

import com.gyp.orderservice.dtos.orderdetail.OrderDetailRequestDto;
import com.gyp.orderservice.dtos.orderdetail.OrderDetailResponseDto;
import com.gyp.orderservice.entities.OrderDetailEntity;
import com.gyp.orderservice.mappers.OrderDetailMapper;
import com.gyp.orderservice.repositories.OrderDetailRepository;
import com.gyp.orderservice.services.OrderDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService {
	private final OrderDetailRepository orderDetailRepository;
	private final OrderDetailMapper orderDetailMapper;

	@Transactional(rollbackFor = Exception.class)
	@Override
	public OrderDetailResponseDto createOrderDetail(OrderDetailRequestDto orderDetailRequestDto) {
		log.info("Creating order detail: {}", orderDetailRequestDto);
		OrderDetailEntity orderDetailEntity = orderDetailMapper.toEntity(orderDetailRequestDto);
		OrderDetailEntity savedOrderDetail = orderDetailRepository.save(orderDetailEntity);
		log.info("Order detail created with ID: {}", savedOrderDetail.getId());
		return orderDetailMapper.toResponseDto(savedOrderDetail);
	}
}
