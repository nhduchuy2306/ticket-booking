package com.gyp.authservice.mappers;

import com.gyp.authservice.dtos.usergroup.UserGroupPermissions;
import com.gyp.authservice.dtos.usergroup.UserGroupRequestDto;
import com.gyp.authservice.dtos.usergroup.UserGroupResponseDto;
import com.gyp.authservice.entities.OrganizationEntity;
import com.gyp.authservice.entities.UserGroupEntity;
import com.gyp.common.converters.Serialization;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UserGroupMapper extends AbstractMapper {
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "organizationEntity", source = "organizationId", qualifiedByName = "mapOrganizationIdToEntity")
	@Mapping(target = "userAccountEntityList", ignore = true)
	@Mapping(target = "userGroupPermissionsRaw", source = "userGroupPermissions",
			qualifiedByName = "mapUserGroupPermissionsRaw")
	UserGroupEntity toEntity(UserGroupRequestDto dto);

	@Mapping(target = "organizationId", source = "entity.organizationEntity.id")
	@Mapping(target = "userGroupPermissions", source = "userGroupPermissionsRaw",
			qualifiedByName = "mapUserGroupPermissions")
	UserGroupResponseDto toDto(UserGroupEntity entity);

	@Named("mapOrganizationIdToEntity")
	default OrganizationEntity mapOrganizationIdToEntity(String organizationId) {
		OrganizationEntity organizationEntity = new OrganizationEntity();
		organizationEntity.setId(organizationId);
		return organizationEntity;
	}

	@Named("mapUserGroupPermissions")
	default UserGroupPermissions mapUserGroupPermissions(String userGroupPermissionsRaw) {
		return Serialization.deserializeFromString(userGroupPermissionsRaw, UserGroupPermissions.class);
	}

	@Named("mapUserGroupPermissionsRaw")
	default String mapUserGroupPermissionsRaw(UserGroupPermissions userGroupPermissions) {
		return Serialization.serializeToString(userGroupPermissions);
	}

	@AfterMapping
	default void afterMapping(@MappingTarget UserGroupEntity entity) {
		mapAbstractFieldsToEntity(entity);
	}
}
