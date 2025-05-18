package com.gyp.authservice.mappers;

import com.gyp.authservice.dtos.auth.RegisterRequestDto;
import com.gyp.authservice.dtos.useraccount.UserAccountRequestDto;
import com.gyp.authservice.dtos.useraccount.UserAccountResponseDto;
import com.gyp.authservice.entities.UserAccountEntity;
import com.gyp.common.models.UserAccountEventModel;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = { UserGroupMapper.class })
public interface UserAccountMapper extends AbstractMapper {
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "userGroupEntityList", source = "userGroupRequestDtoList")
	UserAccountEntity toEntity(UserAccountRequestDto dto);

	@Mapping(target = "userGroupResponseDtoList", source = "userGroupEntityList")
	UserAccountResponseDto toResponseDto(UserAccountEntity entity);

	@Mapping(target = "actions", ignore = true)
	UserAccountEventModel toModel(UserAccountEntity entity);


	@Mapping(target = "id", ignore = true)
	@Mapping(target = "userGroupEntityList", ignore = true)
	UserAccountEntity toEntity(RegisterRequestDto dto);

	@AfterMapping
	default void afterMapping(@MappingTarget UserAccountEntity entity) {
		mapAbstractFieldsToEntity(entity);
	}
}
