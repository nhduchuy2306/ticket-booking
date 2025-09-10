package com.gyp.orderservice.mappers;

import java.util.List;

import com.gyp.orderservice.dtos.order.OrderRequestDto;
import com.gyp.orderservice.dtos.order.OrderResponseDto;
import com.gyp.orderservice.entities.OrderEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingInheritanceStrategy;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", mappingInheritanceStrategy = MappingInheritanceStrategy.AUTO_INHERIT_FROM_CONFIG,
		uses = { OrderDetailMapper.class })
public interface OrderMapper extends AbstractMapper {
	OrderResponseDto toResponseDto(OrderEntity orderEntity);

	List<OrderResponseDto> toResponseDtoList(List<OrderEntity> orderEntities);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "status", ignore = true)
	@Mapping(target = "orderDetailEntityList", ignore = true)
	OrderEntity toEntity(OrderRequestDto orderRequestDto);

	@AfterMapping
	default void afterMapping(@MappingTarget OrderEntity entity) {
		mapAbstractFieldsToEntity(entity);
	}
}
