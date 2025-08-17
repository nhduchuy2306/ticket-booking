package com.gyp.salechannelservice.mappers;

import java.util.List;

import com.gyp.common.mappers.AbstractMapper;
import com.gyp.salechannelservice.dtos.salechannel.SaleChannelRequestDto;
import com.gyp.salechannelservice.dtos.salechannel.SaleChannelResponseDto;
import com.gyp.salechannelservice.entities.SaleChannelEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingInheritanceStrategy;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", mappingInheritanceStrategy = MappingInheritanceStrategy.AUTO_INHERIT_FROM_CONFIG)
public interface SaleChannelMapper extends AbstractMapper {
	SaleChannelResponseDto toResponseDto(SaleChannelEntity entity);

	@Mapping(target = "organizationId", ignore = true)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createUser", ignore = true)
	@Mapping(target = "createTimestamp", ignore = true)
	@Mapping(target = "changeUser", ignore = true)
	@Mapping(target = "changeTimestamp", ignore = true)
	@Mapping(target = "saleChannelEventEntityList", ignore = true)
	@Mapping(target = "saleChannelConfigEntityList", ignore = true)
	SaleChannelEntity toEntity(SaleChannelRequestDto dto);

	List<SaleChannelResponseDto> toResponseDtoList(List<SaleChannelEntity> entities);

	@Mapping(target = "organizationId", ignore = true)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createUser", ignore = true)
	@Mapping(target = "createTimestamp", ignore = true)
	@Mapping(target = "changeUser", ignore = true)
	@Mapping(target = "changeTimestamp", ignore = true)
	@Mapping(target = "saleChannelEventEntityList", ignore = true)
	@Mapping(target = "saleChannelConfigEntityList", ignore = true)
	void updateEntityFromDto(SaleChannelRequestDto dto, @MappingTarget SaleChannelEntity entity);

	@AfterMapping
	default void afterMapping(SaleChannelEntity entity) {
		mapAbstractFieldsToEntity(entity);
	}

	@AfterMapping
	default void afterMapping(@MappingTarget SaleChannelResponseDto responseDto, SaleChannelEntity entity) {
		mapAbstractFields(entity, responseDto);
	}
}
