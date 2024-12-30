package com.example.ticket.authservice.mappers;

import java.util.List;
import java.util.stream.Collectors;

import com.example.ticket.authservice.dtos.useraccount.UserAccountRequestDto;
import com.example.ticket.authservice.dtos.useraccount.UserAccountResponseDto;
import com.example.ticket.authservice.dtos.usergroup.UserGroupRequestDto;
import com.example.ticket.authservice.dtos.usergroup.UserGroupResponseDto;
import com.example.ticket.authservice.entities.UserAccountEntity;
import com.example.ticket.authservice.entities.UserGroupEntity;
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
