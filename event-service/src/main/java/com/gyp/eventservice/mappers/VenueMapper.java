package com.gyp.eventservice.mappers;

import java.util.List;

import com.gyp.eventservice.dtos.venue.VenueRequestDto;
import com.gyp.eventservice.dtos.venue.VenueResponseDto;
import com.gyp.eventservice.entities.VenueEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingInheritanceStrategy;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring",
		mappingInheritanceStrategy = MappingInheritanceStrategy.AUTO_INHERIT_FROM_CONFIG,
		uses = { EventMapper.class, SeatMapMapper.class })
public interface VenueMapper extends AbstractMapper {
	// To response DTO
	@Named("toVenueResponseDto")
	VenueResponseDto toResponseDto(VenueEntity entity);

	// List mappings
	@IterableMapping(qualifiedByName = "toVenueResponseDto")
	List<VenueResponseDto> toResponseDtoList(List<VenueEntity> entities);

	// Create new entity from request
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "eventEntityList", ignore = true)
	@Mapping(target = "seatMapEntityList", ignore = true)
	@Named("toEntity")
	VenueEntity toEntity(VenueRequestDto dto);

	// Update existing entity from request
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "eventEntityList", ignore = true)
	@Mapping(target = "seatMapEntityList", ignore = true)
	@Named("updateEntityFromDto")
	void updateEntityFromDto(VenueRequestDto dto, @MappingTarget VenueEntity entity);

	@AfterMapping
	default void afterMapping(@MappingTarget VenueEntity entity) {
		mapAbstractFieldsToEntity(entity);
	}
}
