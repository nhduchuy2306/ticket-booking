package com.gyp.ticketservice.mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gyp.common.converters.Serialization;
import com.gyp.common.models.SeatMapTicketEM;
import com.gyp.ticketservice.dtos.seatmap.SeatConfig;
import com.gyp.ticketservice.dtos.seatmap.SeatMapDto;
import com.gyp.ticketservice.dtos.seatmap.StageConfig;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingInheritanceStrategy;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", mappingInheritanceStrategy = MappingInheritanceStrategy.AUTO_INHERIT_FROM_CONFIG)
public interface SeatMapMapper extends AbstractMapper {
	@Mapping(target = "stageConfig", source = "seatMapResponse.stageConfig", qualifiedByName = "mapStageConfig")
	@Mapping(target = "seatConfig", source = "seatMapResponse.seatConfig", qualifiedByName = "mapSeatConfig")
	SeatMapDto toDto(SeatMapTicketEM seatMapResponse);

	@Named("mapStageConfig")
	default StageConfig mapStageConfig(String stage) throws JsonProcessingException {
		if(StringUtils.isEmpty(stage)) {
			return null;
		}
		return Serialization.deserializeFromString(stage, StageConfig.class);
	}

	@Named("mapSeatConfig")
	default SeatConfig mapSeatConfig(String seatConfig) throws JsonProcessingException {
		if(StringUtils.isEmpty(seatConfig)) {
			return null;
		}
		return Serialization.deserializeFromString(seatConfig, SeatConfig.class);
	}
}
