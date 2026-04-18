package com.gyp.eventservice.mappers;

import java.util.List;

import com.gyp.common.converters.Serialization;
import com.gyp.common.models.SeatMapEventModel;
import com.gyp.common.models.SeatMapTicketEM;
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
	@Mapping(target = "seatConfigRaw", expression = "java(serializeSeatConfig(dto.getSeatConfig()))")
	@Mapping(target = "stageConfigRaw", expression = "java(serializeStageConfig(dto.getStageConfig()))")
	SeatMapEntity toEntity(SeatMapRequestDto dto);

	@Mapping(target = "stageConfig", source = "stageConfigRaw")
	@Mapping(target = "seatConfig", source = "seatConfigRaw")
	SeatMapEventModel toSeatMapEventModel(SeatMapEntity seatMapEntity);

	@Mapping(target = "eventName", ignore = true)
	@Mapping(target = "eventId", ignore = true)
	@Mapping(target = "eventDateTime", ignore = true)
	@Mapping(target = "stageConfig", source = "stageConfigRaw")
	@Mapping(target = "seatConfig", source = "seatConfigRaw")
	SeatMapTicketEM toSeatMapTicketEM(SeatMapEntity seatMapEntity);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "venueMapEntityList", ignore = true)
	@Mapping(target = "seatConfigRaw", expression = "java(serializeSeatConfig(dto.getSeatConfig()))")
	@Mapping(target = "stageConfigRaw", expression = "java(serializeStageConfig(dto.getStageConfig()))")
	@Mapping(target = "eventSectionMappingEntityList", ignore = true)
	void updateEntityFromDto(SeatMapRequestDto dto, @MappingTarget SeatMapEntity entity);

	default SeatConfig parseSeatConfig(String seatConfigRaw) {
		return Serialization.deserializeFromString(seatConfigRaw, SeatConfig.class);
	}

	default StageConfig parseStageConfig(String stageConfigRaw) {
		return Serialization.deserializeFromString(stageConfigRaw, StageConfig.class);
	}

	default String serializeSeatConfig(SeatConfig seatConfig) {
		return Serialization.serializeToString(seatConfig);
	}

	default String serializeStageConfig(StageConfig stageConfig) {
		return Serialization.serializeToString(stageConfig);
	}

	@AfterMapping
	default void afterMapping(@MappingTarget SeatMapEntity entity) {
		mapAbstractFieldsToEntity(entity);
	}
}
