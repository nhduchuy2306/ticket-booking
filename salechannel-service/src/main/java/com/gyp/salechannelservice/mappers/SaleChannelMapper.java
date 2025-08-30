package com.gyp.salechannelservice.mappers;

import java.util.LinkedHashMap;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gyp.common.converters.Serialization;
import com.gyp.common.enums.salechannel.SaleChannelType;
import com.gyp.common.mappers.AbstractMapper;
import com.gyp.salechannelservice.dtos.salechannel.SaleChannelRequestDto;
import com.gyp.salechannelservice.dtos.salechannel.SaleChannelResponseDto;
import com.gyp.salechannelservice.dtos.salechannelconfig.ApiPartnerSaleChannelConfigDto;
import com.gyp.salechannelservice.dtos.salechannelconfig.BoxOfficeSaleChannelConfigDto;
import com.gyp.salechannelservice.dtos.salechannelconfig.MobileAppSaleChannelConfigDto;
import com.gyp.salechannelservice.dtos.salechannelconfig.SaleChannelConfig;
import com.gyp.salechannelservice.dtos.salechannelconfig.TicketShopSaleChannelConfigDto;
import com.gyp.salechannelservice.entities.SaleChannelEntity;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingInheritanceStrategy;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", mappingInheritanceStrategy = MappingInheritanceStrategy.AUTO_INHERIT_FROM_CONFIG)
public interface SaleChannelMapper extends AbstractMapper {
	@Mapping(target = "saleChannelConfig", source = "saleChannelConfig", qualifiedByName = "mapSaleChannelConfigToDto")
	SaleChannelResponseDto toResponseDto(SaleChannelEntity entity);

	@Mapping(target = "organizationId", ignore = true)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createUser", ignore = true)
	@Mapping(target = "createTimestamp", ignore = true)
	@Mapping(target = "changeUser", ignore = true)
	@Mapping(target = "changeTimestamp", ignore = true)
	@Mapping(target = "saleChannelEventEntityList", ignore = true)
	@Mapping(target = "saleChannelConfig", source = "saleChannelConfig", qualifiedByName = "mapSaleChannelConfigToJson")
	SaleChannelEntity toEntity(SaleChannelRequestDto dto);

	List<SaleChannelResponseDto> toResponseDtoList(List<SaleChannelEntity> entities);

	@Mapping(target = "organizationId", ignore = true)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createUser", ignore = true)
	@Mapping(target = "createTimestamp", ignore = true)
	@Mapping(target = "changeUser", ignore = true)
	@Mapping(target = "changeTimestamp", ignore = true)
	@Mapping(target = "saleChannelEventEntityList", ignore = true)
	@Mapping(target = "saleChannelConfig", source = "saleChannelConfig", qualifiedByName = "mapSaleChannelConfigToJson")
	void updateEntityFromDto(SaleChannelRequestDto dto, @MappingTarget SaleChannelEntity entity);

	@Named("mapSaleChannelConfigToDto")
	default SaleChannelConfig mapSaleChannelConfigToDto(String configJson) {
		try {
			if(StringUtils.isEmpty(configJson)) {
				return null;
			}
			LinkedHashMap object = Serialization.deserializeFromString(configJson);
			String type = (String) object.get("type");
			if(SaleChannelType.TICKET_SHOP.name().equals(type)) {
				return Serialization.deserializeFromString(configJson, TicketShopSaleChannelConfigDto.class);
			} else if(SaleChannelType.BOX_OFFICE.name().equals(type)) {
				return Serialization.deserializeFromString(configJson, BoxOfficeSaleChannelConfigDto.class);
			} else if(SaleChannelType.API_PARTNER.name().equals(type)) {
				return Serialization.deserializeFromString(configJson, ApiPartnerSaleChannelConfigDto.class);
			} else if(SaleChannelType.MOBILE_APP.name().equals(type)) {
				return Serialization.deserializeFromString(configJson, MobileAppSaleChannelConfigDto.class);
			}
			return null;
		} catch(JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	@Named("mapSaleChannelConfigToJson")
	default String mapSaleChannelConfigToJson(SaleChannelConfig config) {
		try {
			if(ObjectUtils.isEmpty(config)) {
				return null;
			}
			return Serialization.serializeToString(config);
		} catch(JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	@AfterMapping
	default void afterMapping(SaleChannelEntity entity) {
		mapAbstractFieldsToEntity(entity);
	}

	@AfterMapping
	default void afterMapping(@MappingTarget SaleChannelResponseDto responseDto, SaleChannelEntity entity) {
		mapAbstractFields(entity, responseDto);
	}
}
