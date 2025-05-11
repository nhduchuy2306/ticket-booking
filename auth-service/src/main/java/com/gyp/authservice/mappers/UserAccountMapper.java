package com.gyp.authservice.mappers;

import com.gyp.authservice.dtos.useraccount.UserAccountRequestDto;
import com.gyp.authservice.dtos.useraccount.UserAccountResponseDto;
import com.gyp.authservice.entities.UserAccountEntity;
import com.gyp.common.models.UserAccountModel;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = { UserGroupMapper.class })
public interface UserAccountMapper extends AbstractMapper {
	@Mapping(target = "id", expression = "java(generateUuid())")
	@Mapping(target = "userGroupEntityList", source = "userGroupRequestDtoList")
	UserAccountEntity toEntity(UserAccountRequestDto dto);

	@Mapping(target = "userGroupResponseDtoList", source = "userGroupEntityList")
	UserAccountResponseDto toDto(UserAccountEntity entity);

	@Mapping(target = "actions", ignore = true)
	UserAccountModel toModel(UserAccountEntity entity);

	@AfterMapping
	default void afterMapping(@MappingTarget UserAccountEntity entity) {
		mapAbstractFieldsToEntity(entity);
	}
}
