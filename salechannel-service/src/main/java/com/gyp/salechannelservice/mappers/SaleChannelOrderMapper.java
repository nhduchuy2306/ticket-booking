package com.gyp.salechannelservice.mappers;

import java.util.List;

import com.gyp.common.mappers.AbstractMapper;
import com.gyp.salechannelservice.dtos.salechannelorder.SaleChannelOrderRequestDto;
import com.gyp.salechannelservice.dtos.salechannelorder.SaleChannelOrderResponseDto;
import com.gyp.salechannelservice.entities.SaleChannelEntity;
import com.gyp.salechannelservice.entities.SaleChannelOrderEntity;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingInheritanceStrategy;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", mappingInheritanceStrategy = MappingInheritanceStrategy.AUTO_INHERIT_FROM_CONFIG)
public interface SaleChannelOrderMapper extends AbstractMapper {
	@Mapping(target = "saleChannelId", source = "saleChannel.id")
	SaleChannelOrderResponseDto toResponseDto(SaleChannelOrderEntity entity);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "saleChannel", source = "saleChannelId", qualifiedByName = "saleChannelIdToSaleChannel")
	SaleChannelOrderEntity toEntity(SaleChannelOrderRequestDto dto);

	List<SaleChannelOrderResponseDto> toResponseDtoList(List<SaleChannelOrderEntity> entities);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "saleChannel", source = "saleChannelId", qualifiedByName = "saleChannelIdToSaleChannel")
	void updateEntityFromDto(SaleChannelOrderRequestDto dto, @MappingTarget SaleChannelOrderEntity entity);

	@AfterMapping
	default void afterMapping(SaleChannelOrderEntity entity) {
		mapAbstractFieldsToEntity(entity);
	}

	@AfterMapping
	default void afterMapping(@MappingTarget SaleChannelOrderResponseDto responseDto, SaleChannelOrderEntity entity) {
		mapAbstractFields(entity, responseDto);
	}

	@Named("saleChannelIdToSaleChannel")
	default SaleChannelEntity saleChannelIdToSaleChannel(String saleChannelId) {
		if(StringUtils.isEmpty(saleChannelId)) {
			return null;
		}
		SaleChannelEntity saleChannelEntity = new SaleChannelEntity();
		saleChannelEntity.setId(saleChannelId);
		return saleChannelEntity;
	}
}
