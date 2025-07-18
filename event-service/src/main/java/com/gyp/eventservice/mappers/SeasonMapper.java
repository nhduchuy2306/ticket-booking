package com.gyp.eventservice.mappers;

import java.util.List;

import com.gyp.eventservice.dtos.season.SeasonRequestDto;
import com.gyp.eventservice.dtos.season.SeasonResponseDto;
import com.gyp.eventservice.entities.CategoryEntity;
import com.gyp.eventservice.entities.SeasonEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingInheritanceStrategy;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", mappingInheritanceStrategy = MappingInheritanceStrategy.AUTO_INHERIT_FROM_CONFIG)
public interface SeasonMapper extends AbstractMapper {
	SeasonResponseDto toResponseDto(SeasonEntity entity);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "eventEntityList", ignore = true)
	SeasonEntity toEntity(SeasonRequestDto dto);

	List<SeasonResponseDto> toResponseDtoList(List<SeasonEntity> entities);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "eventEntityList", ignore = true)
	void updateEntityFromDto(SeasonRequestDto dto, @MappingTarget SeasonEntity entity);

	@AfterMapping
	default void afterMapping(@MappingTarget CategoryEntity entity) {
		mapAbstractFieldsToEntity(entity);
	}

	@AfterMapping
	default void afterMapping(@MappingTarget SeasonResponseDto responseDto, SeasonEntity entity) {
		mapAbstractFields(entity, responseDto);
	}
}
