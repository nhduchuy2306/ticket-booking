package com.gyp.authservice.mappers;

import com.gyp.authservice.dtos.organization.OrganizationRequestDto;
import com.gyp.authservice.dtos.organization.OrganizationResponseDto;
import com.gyp.authservice.entities.OrganizationEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OrganizationMapper extends AbstractMapper {
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "userAccountEntityList", ignore = true)
	@Mapping(target = "userGroupEntityList", ignore = true)
	OrganizationEntity toEntity(OrganizationRequestDto dto);

	OrganizationResponseDto toResponseDto(OrganizationEntity entity);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "userAccountEntityList", ignore = true)
	@Mapping(target = "userGroupEntityList", ignore = true)
	void updateEntityFromDto(OrganizationRequestDto dto, @MappingTarget OrganizationEntity entity);

	@AfterMapping
	default void afterMapping(@MappingTarget OrganizationEntity entity) {
		mapAbstractFieldsToEntity(entity);
	}
}
