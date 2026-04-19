package com.gyp.authservice.mappers;

import com.gyp.authservice.dtos.organization.OrganizationRequestDto;
import com.gyp.authservice.dtos.organization.OrganizationResponseDto;
import com.gyp.authservice.entities.OrganizationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OrganizationMapper extends AbstractMapper {
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "userAccountEntityList", ignore = true)
	@Mapping(target = "userGroupEntityList", ignore = true)
	@Mapping(target = "description", ignore = true)
	@Mapping(target = "name", source = "orgName")
	@Mapping(target = "orgSlug", source = "orgSlug")
	@Mapping(target = "businessEmail", source = "businessEmail")
	@Mapping(target = "phone", source = "phone")
	@Mapping(target = "address", source = "address")
	@Mapping(target = "taxCode", source = "taxCode")
	@Mapping(target = "representativeName", source = "representativeName")
	@Mapping(target = "representativePhone", source = "representativePhone")
	@Mapping(target = "status", ignore = true)
	OrganizationEntity toEntity(OrganizationRequestDto dto);

	@Mapping(target = "orgName", source = "name")
	@Mapping(target = "message", ignore = true)
	OrganizationResponseDto toResponseDto(OrganizationEntity entity);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "userAccountEntityList", ignore = true)
	@Mapping(target = "userGroupEntityList", ignore = true)
	@Mapping(target = "description", ignore = true)
	@Mapping(target = "name", source = "orgName")
	@Mapping(target = "orgSlug", source = "orgSlug")
	@Mapping(target = "businessEmail", source = "businessEmail")
	@Mapping(target = "phone", source = "phone")
	@Mapping(target = "address", source = "address")
	@Mapping(target = "taxCode", source = "taxCode")
	@Mapping(target = "representativeName", source = "representativeName")
	@Mapping(target = "representativePhone", source = "representativePhone")
	@Mapping(target = "status", ignore = true)
	void updateEntityFromDto(OrganizationRequestDto dto, @MappingTarget OrganizationEntity entity);

}
