package com.gyp.eventservice.mappers;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gyp.common.converters.Serialization;
import com.gyp.eventservice.dtos.seatmap.SeatConfig;
import com.gyp.eventservice.dtos.seatmap.SeatMapRequestDto;
import com.gyp.eventservice.dtos.seatmap.SeatMapResponseDto;
import com.gyp.eventservice.dtos.seatmap.StageConfig;
import com.gyp.eventservice.entities.SeatMapEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingInheritanceStrategy;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring",
		mappingInheritanceStrategy = MappingInheritanceStrategy.AUTO_INHERIT_FROM_CONFIG,
		uses = { EventMapper.class, VenueMapper.class })
public interface SeatMapMapper extends AbstractMapper {
	@Mapping(target = "seatConfig", expression = "java(parseSeatConfig(entity.getSeatConfigRaw()))")
	@Mapping(target = "stageConfig", expression = "java(parseStageConfig(entity.getStageConfigRaw()))")
	SeatMapResponseDto toResponseDto(SeatMapEntity entity);

	List<SeatMapResponseDto> toResponseDtoList(List<SeatMapEntity> entities);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "venueMapEntityList", ignore = true)
	@Mapping(target = "seatConfigRaw", expression = "java(parseSeatConfigRaw(dto.getSeatConfig()))")
	@Mapping(target = "stageConfigRaw", expression = "java(parseStageConfig(dto.getStageConfig()))")
	SeatMapEntity toEntity(SeatMapRequestDto dto);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "venueMapEntityList", ignore = true)
	@Mapping(target = "seatConfigRaw", expression = "java(parseSeatConfigRaw(dto.getSeatConfig()))")
	@Mapping(target = "stageConfigRaw", expression = "java(parseStageConfig(dto.getStageConfig()))")
	void updateEntityFromDto(SeatMapRequestDto dto, @MappingTarget SeatMapEntity entity);

	default SeatConfig parseSeatConfig(String seatConfigRaw) {
		try {
			return Serialization.deserializeFromString(seatConfigRaw, SeatConfig.class);
		} catch(JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	default StageConfig parseStageConfig(String stageConfigRaw) {
		try {
			return Serialization.deserializeFromString(stageConfigRaw, StageConfig.class);
		} catch(JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	default String parseSeatConfigRaw(SeatConfig seatConfig) {
		try {
			return Serialization.serializeToString(seatConfig);
		} catch(JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	default String parseStageConfig(StageConfig stageConfig) {
		try {
			return Serialization.serializeToString(stageConfig);
		} catch(JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	@AfterMapping
	default void afterMapping(@MappingTarget SeatMapEntity entity) {
		mapAbstractFieldsToEntity(entity);
	}
}
