package com.gyp.authservice.mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gyp.authservice.dtos.usergroup.UserGroupPermissions;
import com.gyp.authservice.dtos.usergroup.UserGroupRequestDto;
import com.gyp.authservice.dtos.usergroup.UserGroupResponseDto;
import com.gyp.authservice.entities.UserGroupEntity;
import com.gyp.common.converters.Serialization;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UserGroupMapper extends AbstractMapper {
	@Mapping(target = "id", expression = "java(generateUuid())")
	@Mapping(target = "userAccountEntityList", ignore = true)
	@Mapping(target = "userGroupPermissionsRaw", source = "userGroupPermissions",
			qualifiedByName = "mapUserGroupPermissionsRaw")
	UserGroupEntity toEntity(UserGroupRequestDto dto);

	@Mapping(target = "userGroupPermissions", source = "userGroupPermissionsRaw",
			qualifiedByName = "mapUserGroupPermissions")
	UserGroupResponseDto toDto(UserGroupEntity entity);

	@Named("mapUserGroupPermissions")
	default UserGroupPermissions mapUserGroupPermissions(String userGroupPermissionsRaw) {
		try {
			return Serialization.deserializeFromString(userGroupPermissionsRaw, UserGroupPermissions.class);
		} catch(JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	@Named("mapUserGroupPermissionsRaw")
	default String mapUserGroupPermissionsRaw(UserGroupPermissions userGroupPermissions) {
		try {
			return Serialization.serializeToString(userGroupPermissions);
		} catch(JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	@AfterMapping
	default void afterMapping(@MappingTarget UserGroupEntity entity) {
		mapAbstractFieldsToEntity(entity);
	}
}
