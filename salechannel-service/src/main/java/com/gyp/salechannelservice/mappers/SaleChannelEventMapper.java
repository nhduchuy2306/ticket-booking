package com.gyp.salechannelservice.mappers;

import java.util.List;

import com.gyp.common.mappers.AbstractMapper;
import com.gyp.salechannelservice.dtos.salechannelevent.SaleChannelEventRequestDto;
import com.gyp.salechannelservice.dtos.salechannelevent.SaleChannelEventResponseDto;
import com.gyp.salechannelservice.entities.SaleChannelEntity;
import com.gyp.salechannelservice.entities.SaleChannelEventEntity;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingInheritanceStrategy;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", mappingInheritanceStrategy = MappingInheritanceStrategy.AUTO_INHERIT_FROM_CONFIG,
		uses = { SaleChannelMapper.class })
public interface SaleChannelEventMapper extends AbstractMapper {
	@Mapping(target = "saleChannel", source = "saleChannelEntity")
	SaleChannelEventResponseDto toResponseDto(SaleChannelEventEntity saleChannelEventEntity);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "saleChannelEntity", source = "saleChannelId", qualifiedByName = "saleChannelIdToEntity")
	SaleChannelEventEntity toEntity(SaleChannelEventRequestDto saleChannelEventRequestDto);

	List<SaleChannelEventResponseDto> toResponseDtoList(List<SaleChannelEventEntity> saleChannelEventEntities);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "saleChannelEntity", source = "saleChannelId", qualifiedByName = "saleChannelIdToEntity")
	void updateEntityFromDto(SaleChannelEventRequestDto saleChannelEventRequestDto,
			@MappingTarget SaleChannelEventEntity saleChannelEventEntity);

	@AfterMapping
	default void afterMapping(SaleChannelEventEntity entity) {
		mapAbstractFieldsToEntity(entity);
	}

	@AfterMapping
	default void afterMapping(@MappingTarget SaleChannelEventResponseDto responseDto, SaleChannelEventEntity entity) {
		mapAbstractFields(entity, responseDto);
	}

	@Named("saleChannelIdToEntity")
	default SaleChannelEntity saleChannelIdToEntity(String saleChannelId) {
		if(StringUtils.isEmpty(saleChannelId)) {
			return null;
		}
		SaleChannelEntity saleChannelEntity = new SaleChannelEntity();
		saleChannelEntity.setId(saleChannelId);
		return saleChannelEntity;
	}
}
