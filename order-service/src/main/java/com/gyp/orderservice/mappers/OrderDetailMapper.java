package com.gyp.orderservice.mappers;

import java.util.List;

import com.gyp.orderservice.dtos.orderdetail.OrderDetailRequestDto;
import com.gyp.orderservice.dtos.orderdetail.OrderDetailResponseDto;
import com.gyp.orderservice.entities.OrderDetailEntity;
import com.gyp.orderservice.entities.OrderEntity;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingInheritanceStrategy;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", mappingInheritanceStrategy = MappingInheritanceStrategy.AUTO_INHERIT_FROM_CONFIG)
public interface OrderDetailMapper extends AbstractMapper {
	@Mapping(target = "orderId", source = "orderEntity.id")
	OrderDetailResponseDto toResponseDto(OrderDetailEntity orderEntity);

	List<OrderDetailResponseDto> toResponseDtoList(List<OrderDetailEntity> orderDetailEntityList);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "orderEntity", source = "orderId", qualifiedByName = "mapOrderIdToOrderEntity")
	OrderDetailEntity toEntity(OrderDetailRequestDto orderDetailRequestDto);

	@Named("mapOrderIdToOrderEntity")
	default OrderEntity mapOrderIdToOrderEntity(String orderId) {
		if(StringUtils.isEmpty(orderId)) {
			return null;
		}
		OrderEntity orderEntity = new OrderEntity();
		orderEntity.setId(orderId);
		return orderEntity;
	}

	@AfterMapping
	default void afterMapping(@MappingTarget OrderEntity entity) {
		mapAbstractFieldsToEntity(entity);
	}
}
