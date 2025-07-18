package com.gyp.eventservice.mappers;

import com.gyp.eventservice.dtos.venuemap.VenueMapRequestDto;
import com.gyp.eventservice.dtos.venuemap.VenueMapResponseDto;
import com.gyp.eventservice.entities.VenueEntity;
import com.gyp.eventservice.entities.VenueMapEntity;
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
	@Mapping(target = "venueEntity", source = "venueId", qualifiedByName = "venueIdToEntity")
	@Mapping(target = "seatMapEntityList", ignore = true)
	VenueMapEntity toEntity(VenueMapRequestDto requestDto);

	VenueMapResponseDto toResponseDto(VenueMapEntity venueMapEntity);

	@Mapping(target = "seatMapEntityList", ignore = true)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "venueEntity", ignore = true)
	void updateEntityFromDto(VenueMapRequestDto requestDto, @MappingTarget VenueMapEntity venueMapEntity);

	@AfterMapping
	default void afterMapping(@MappingTarget VenueEntity entity) {
		mapAbstractFieldsToEntity(entity);
	}

	@Named("venueIdToEntity")
	default VenueEntity venueIdToEntity(String venueId) {
		if(venueId == null || venueId.isBlank()) {
			return null;
		}
		VenueEntity venueEntity = new VenueEntity();
		venueEntity.setId(venueId);
		return venueEntity;
	}
}
