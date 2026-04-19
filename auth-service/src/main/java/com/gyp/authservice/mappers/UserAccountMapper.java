package com.gyp.authservice.mappers;

import com.gyp.authservice.dtos.auth.RegisterRequestDto;
import com.gyp.authservice.dtos.useraccount.UserAccountRequestDto;
import com.gyp.authservice.dtos.useraccount.UserAccountResponseDto;
import com.gyp.authservice.entities.UserAccountEntity;
import com.gyp.common.models.UserAccountEventModel;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = { UserGroupMapper.class }, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserAccountMapper extends AbstractMapper {
	@BeanMapping(ignoreByDefault = true)
	@Mapping(target = "name", source = "name")
	@Mapping(target = "username", source = "username")
	@Mapping(target = "password", source = "password")
	@Mapping(target = "dob", source = "dob")
	@Mapping(target = "phoneNumber", source = "phoneNumber")
	@Mapping(target = "email", source = "email")
	@Mapping(target = "userGroupEntityList", ignore = true)
	@Mapping(target = "realmType", ignore = true)
	@Mapping(target = "id", ignore = true)
	UserAccountEntity toEntity(UserAccountRequestDto dto);

	@BeanMapping(ignoreByDefault = true)
	@Mapping(target = "name", source = "name")
	@Mapping(target = "username", source = "username")
	@Mapping(target = "dob", source = "dob")
	@Mapping(target = "phoneNumber", source = "phoneNumber")
	@Mapping(target = "email", source = "email")
	@Mapping(target = "organizationId", source = "organizationId")
	@Mapping(target = "realmType", source = "realmType")
	@Mapping(target = "userGroupList", source = "userGroupEntityList")
	UserAccountResponseDto toResponseDto(UserAccountEntity entity);

	@Mapping(target = "actions", ignore = true)
	UserAccountEventModel toModel(UserAccountEntity entity);

	@BeanMapping(ignoreByDefault = true)
	@Mapping(target = "name", source = "name")
	@Mapping(target = "username", source = "username")
	@Mapping(target = "password", source = "password")
	@Mapping(target = "dob", source = "dob")
	@Mapping(target = "phoneNumber", source = "phoneNumber")
	@Mapping(target = "email", source = "email")
	@Mapping(target = "userGroupEntityList", ignore = true)
	@Mapping(target = "realmType", ignore = true)
	@Mapping(target = "id", ignore = true)
	UserAccountEntity toEntity(RegisterRequestDto dto);

	@BeanMapping(ignoreByDefault = true)
	@Mapping(target = "name", source = "name")
	@Mapping(target = "username", source = "username")
	@Mapping(target = "dob", source = "dob")
	@Mapping(target = "phoneNumber", source = "phoneNumber")
	@Mapping(target = "email", source = "email")
	@Mapping(target = "userGroupEntityList", ignore = true)
	@Mapping(target = "realmType", ignore = true)
	@Mapping(target = "id", ignore = true)
	void updateEntityFromDto(UserAccountRequestDto dto, @MappingTarget UserAccountEntity entity);
}
