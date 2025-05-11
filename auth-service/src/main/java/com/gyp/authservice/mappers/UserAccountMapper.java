package com.gyp.authservice.mappers;

import com.gyp.authservice.dtos.useraccount.UserAccountRequestDto;
import com.gyp.authservice.dtos.useraccount.UserAccountResponseDto;
import com.gyp.authservice.entities.UserAccountEntity;
import com.gyp.common.models.UserAccountModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { UserGroupMapper.class })
public interface UserAccountMapper {
	@Mapping(target = "userGroupEntityList", source = "userGroupRequestDtoList")
	UserAccountEntity toEntity(UserAccountRequestDto dto);

	@Mapping(target = "userGroupResponseDtoList", source = "userGroupEntityList")
	UserAccountResponseDto toDto(UserAccountEntity entity);

	@Mapping(target = "actions", ignore = true)
	UserAccountModel toModel(UserAccountEntity entity);

	//	@Named("mapUserGroupEntities")
	//	default List<UserGroupEntity> mapUserGroupEntities(List<UserGroupRequestDto> userGroupRequestDtoList) {
	//		return userGroupRequestDtoList.stream()
	//				.map(userGroupMapper::toEntity)
	//				.collect(Collectors.toList());
	//	}
	//
	//	@Named("mapUserGroupResponseDtoList")
	//	default List<UserGroupResponseDto> mapUserGroupResponseDtoList(List<UserGroupEntity> userGroupEntityList) {
	//		return userGroupEntityList.stream()
	//				.map(UserGroupMapper.INSTANCE::toDto)
	//				.collect(Collectors.toList());
	//	}
}
