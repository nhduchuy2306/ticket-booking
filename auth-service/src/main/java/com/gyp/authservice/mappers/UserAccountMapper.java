package com.gyp.authservice.mappers;

import com.gyp.authservice.dtos.auth.RegisterRequestDto;
import com.gyp.authservice.dtos.useraccount.UserAccountRequestDto;
import com.gyp.authservice.dtos.useraccount.UserAccountResponseDto;
import com.gyp.authservice.entities.OrganizationEntity;
import com.gyp.authservice.entities.UserAccountEntity;
import com.gyp.common.models.UserAccountEventModel;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = { UserGroupMapper.class })
public interface UserAccountMapper extends AbstractMapper {
	@Mapping(target = "organizationEntity", source = "organizationId", qualifiedByName = "toOrganizationEntity")
	@Mapping(target = "realmType", ignore = true)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "userGroupEntityList", ignore = true)
	UserAccountEntity toEntity(UserAccountRequestDto dto);

	@Mapping(target = "organizationId", source = "entity.organizationEntity.id")
	@Mapping(target = "userGroupList", source = "userGroupEntityList")
	UserAccountResponseDto toResponseDto(UserAccountEntity entity);

	@Mapping(target = "actions", ignore = true)
	UserAccountEventModel toModel(UserAccountEntity entity);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "organizationEntity", ignore = true)
	@Mapping(target = "userGroupEntityList", ignore = true)
	UserAccountEntity toEntity(RegisterRequestDto dto);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "userGroupEntityList", ignore = true)
	@Mapping(target = "organizationEntity", ignore = true)
	void updateEntityFromDto(UserAccountRequestDto dto, @MappingTarget UserAccountEntity entity);

	@AfterMapping
	default void afterMapping(@MappingTarget UserAccountEntity entity) {
		mapAbstractFieldsToEntity(entity);
	}

	@Named("toOrganizationEntity")
	default OrganizationEntity toOrganizationEntity(String organizationId) {
		if(StringUtils.isEmpty(organizationId)) {
			return null;
		}
		OrganizationEntity organizationEntity = new OrganizationEntity();
		organizationEntity.setId(organizationId);
		return organizationEntity;
	}
}
