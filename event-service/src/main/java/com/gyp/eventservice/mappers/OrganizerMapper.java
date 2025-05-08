package com.gyp.eventservice.mappers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

import com.gyp.common.models.UserAccountModel;
import com.gyp.eventservice.dtos.organizer.OrganizerRequestDto;
import com.gyp.eventservice.dtos.organizer.OrganizerResponseDto;
import com.gyp.eventservice.entities.OrganizerEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingInheritanceStrategy;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring",
		mappingInheritanceStrategy = MappingInheritanceStrategy.AUTO_INHERIT_FROM_CONFIG,
		uses = { EventMapper.class })
public interface OrganizerMapper extends AbstractMapper {
	// To response DTO
	@Mapping(target = "age", expression = "java(calculateAge(entity.getDob()))")
	OrganizerResponseDto toResponseDto(OrganizerEntity entity);

	// List mappings
	List<OrganizerResponseDto> toResponseDtoList(List<OrganizerEntity> entities);

	// Create new entity from request
	@Mapping(target = "eventEntityList", ignore = true)
	@Named("toEntity")
	OrganizerEntity toEntity(OrganizerRequestDto dto);

	@Mapping(target = "eventEntityList", ignore = true)
	@Named("toEntity")
	OrganizerEntity toEntity(UserAccountModel userAccountModel);

	// Update existing entity from request
	@Mapping(target = "eventEntityList", ignore = true)
	@Named("updateEntityFromDto")
	void updateEntityFromDto(OrganizerRequestDto dto, @MappingTarget OrganizerEntity entity);

	// Helper method for age calculation
	default Integer calculateAge(LocalDateTime dob) {
		if(dob == null) {
			return null;
		}
		return Period.between(dob.toLocalDate(), LocalDate.now()).getYears();
	}

	@AfterMapping
	default void afterMapping(@MappingTarget OrganizerEntity entity) {
		mapAbstractFieldsToEntity(entity);
	}
}
