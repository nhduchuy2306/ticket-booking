package com.gyp.eventservice.mappers;

import java.util.List;

import com.gyp.eventservice.dtos.venuemap.VenueMapRequestDto;
import com.gyp.eventservice.dtos.venuemap.VenueMapResponseDto;
import com.gyp.eventservice.entities.SeatMapEntity;
import com.gyp.eventservice.entities.VenueEntity;
import com.gyp.eventservice.entities.VenueMapEntity;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingInheritanceStrategy;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring",
		mappingInheritanceStrategy = MappingInheritanceStrategy.AUTO_INHERIT_FROM_CONFIG,
		uses = { VenueMapper.class })
public interface VenueMapMapper extends AbstractMapper {

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "eventEntityList", ignore = true)
	@Mapping(target = "seatMapEntity", source = "seatMapId", qualifiedByName = "seatMapIdToEntity")
	@Mapping(target = "venueEntity", source = "venueId", qualifiedByName = "venueIdToEntity")
	VenueMapEntity toEntity(VenueMapRequestDto requestDto);

	@Mapping(target = "venueName", source = "venueMapEntity.venueEntity.name")
	@Mapping(target = "seatMapName", source = "venueMapEntity.seatMapEntity.name")
	@Mapping(target = "venueId", source = "venueMapEntity.venueEntity.id")
	@Mapping(target = "seatMapId", source = "venueMapEntity.seatMapEntity.id")
	VenueMapResponseDto toResponseDto(VenueMapEntity venueMapEntity);

	List<VenueMapResponseDto> toResponseDtoList(List<VenueMapEntity> venueMapEntities);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "eventEntityList", ignore = true)
	@Mapping(target = "seatMapEntity", source = "seatMapId", qualifiedByName = "seatMapIdToEntity")
	@Mapping(target = "venueEntity", source = "venueId", qualifiedByName = "venueIdToEntity")
	void updateEntityFromDto(VenueMapRequestDto requestDto, @MappingTarget VenueMapEntity venueMapEntity);

	@AfterMapping
	default void afterMapping(@MappingTarget VenueEntity entity) {
		mapAbstractFieldsToEntity(entity);
	}

	@AfterMapping
	default void afterMapping(@MappingTarget VenueMapResponseDto responseDto, VenueMapEntity entity) {
		mapAbstractFields(entity, responseDto);
	}

	@Named("venueIdToEntity")
	default VenueEntity venueIdToEntity(String venueId) {
		if(StringUtils.isEmpty(venueId)) {
			return null;
		}
		VenueEntity venueEntity = new VenueEntity();
		venueEntity.setId(venueId);
		return venueEntity;
	}

	@Named("seatMapIdToEntity")
	default SeatMapEntity seatMapIdToEntity(String seatMapId) {
		if(StringUtils.isEmpty(seatMapId)) {
			return null;
		}
		SeatMapEntity seatMapEntity = new SeatMapEntity();
		seatMapEntity.setId(seatMapId);
		return seatMapEntity;
	}
}
