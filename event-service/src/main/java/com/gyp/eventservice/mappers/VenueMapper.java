package com.gyp.eventservice.mappers;

import java.util.List;

import com.gyp.eventservice.dtos.venue.VenueRequestDto;
import com.gyp.eventservice.dtos.venue.VenueResponseDto;
import com.gyp.eventservice.entities.VenueEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingInheritanceStrategy;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring",
		mappingInheritanceStrategy = MappingInheritanceStrategy.AUTO_INHERIT_FROM_CONFIG,
		uses = { EventMapper.class, SeatMapMapper.class })
public interface VenueMapper extends AbstractMapper {
	// To response DTO
	@Mapping(target = "venueMapList", source = "entity.venueMapEntityList")
	VenueResponseDto toResponseDto(VenueEntity entity);

	// List mappings
	List<VenueResponseDto> toResponseDtoList(List<VenueEntity> entities);

	// Create new entity from request
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "venueMapEntityList", ignore = true)
	@Mapping(target = "eventEntityList", ignore = true)
	VenueEntity toEntity(VenueRequestDto dto);

	// Update existing entity from request
	@Mapping(target = "venueMapEntityList", ignore = true)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "eventEntityList", ignore = true)
	void updateEntityFromDto(VenueRequestDto dto, @MappingTarget VenueEntity entity);

	@AfterMapping
	default void afterMapping(@MappingTarget VenueEntity entity) {
		mapAbstractFieldsToEntity(entity);
	}

	@AfterMapping
	default void afterMapping(@MappingTarget VenueResponseDto responseDto, VenueEntity entity) {
		mapAbstractFields(entity, responseDto);
	}
}
