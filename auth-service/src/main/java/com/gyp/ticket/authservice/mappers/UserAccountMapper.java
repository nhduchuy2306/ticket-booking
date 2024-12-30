package com.gyp.ticket.authservice.mappers;

import java.util.List;
import java.util.stream.Collectors;

import com.gyp.common.models.UserAccountModel;
import com.gyp.ticket.authservice.dtos.useraccount.UserAccountRequestDto;
import com.gyp.ticket.authservice.dtos.useraccount.UserAccountResponseDto;
import com.gyp.ticket.authservice.dtos.usergroup.UserGroupRequestDto;
import com.gyp.ticket.authservice.dtos.usergroup.UserGroupResponseDto;
import com.gyp.ticket.authservice.entities.UserAccountEntity;
import com.gyp.ticket.authservice.entities.UserGroupEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserAccountMapper {
	UserAccountMapper INSTANCE = Mappers.getMapper(UserAccountMapper.class);

	@Mapping(target = "userGroupEntityList", source = "userGroupRequestDtoList",
			qualifiedByName = "mapUserGroupEntities")
	UserAccountEntity toEntity(UserAccountRequestDto dto);

	@Mapping(target = "userGroupResponseDtoList", source = "userGroupEntityList",
			qualifiedByName = "mapUserGroupResponseDtoList")
	UserAccountResponseDto toDto(UserAccountEntity entity);

	@Mapping(target = "actions", ignore = true)
	UserAccountModel toModel(UserAccountEntity entity);

	@Named("mapUserGroupEntities")
	default List<UserGroupEntity> mapUserGroupEntities(List<UserGroupRequestDto> userGroupRequestDtoList) {
		return userGroupRequestDtoList.stream()
				.map(UserGroupMapper.INSTANCE::toEntity)
				.collect(Collectors.toList());
	}

	@Named("mapUserGroupResponseDtoList")
	default List<UserGroupResponseDto> mapUserGroupResponseDtoList(List<UserGroupEntity> userGroupEntityList) {
		return userGroupEntityList.stream()
				.map(UserGroupMapper.INSTANCE::toDto)
				.collect(Collectors.toList());
	}
}
