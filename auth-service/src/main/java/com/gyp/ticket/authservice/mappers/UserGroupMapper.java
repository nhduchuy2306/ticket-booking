package com.gyp.ticket.authservice.mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gyp.common.converters.Serialization;
import com.gyp.ticket.authservice.dtos.usergroup.UserGroupPermissions;
import com.gyp.ticket.authservice.dtos.usergroup.UserGroupRequestDto;
import com.gyp.ticket.authservice.dtos.usergroup.UserGroupResponseDto;
import com.gyp.ticket.authservice.entities.UserGroupEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserGroupMapper {
	UserGroupMapper INSTANCE = Mappers.getMapper(UserGroupMapper.class);

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
}
