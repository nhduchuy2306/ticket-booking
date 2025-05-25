package com.gyp.authservice.services.impl;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gyp.authservice.dtos.usergroup.ApplicationPermissionDto;
import com.gyp.authservice.dtos.usergroup.UserGroupPermissions;
import com.gyp.authservice.dtos.usergroup.UserGroupRequestDto;
import com.gyp.authservice.dtos.usergroup.UserGroupResponseDto;
import com.gyp.authservice.entities.UserGroupEntity;
import com.gyp.authservice.mappers.UserGroupMapper;
import com.gyp.authservice.repositories.UserGroupRepository;
import com.gyp.authservice.services.UserGroupService;
import com.gyp.common.converters.Serialization;
import com.gyp.common.enums.permission.ApplicationPermission;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserGroupServiceImpl implements UserGroupService {
	private final UserGroupRepository userGroupRepository;
	private final UserGroupMapper userGroupMapper;

	@Override
	public List<UserGroupResponseDto> getListUserGroups() {
		List<UserGroupEntity> userGroupEntities = userGroupRepository.findAll();
		return userGroupEntities.stream().map(userGroupMapper::toDto).toList();
	}

	@Override
	public UserGroupResponseDto getUserGroupById(String id) {
		UserGroupEntity userGroupEntity = userGroupRepository.findById(id).orElse(null);
		if(userGroupEntity != null) {
			return userGroupMapper.toDto(userGroupEntity);
		}
		return null;
	}

	@Override
	public void createUserGroup(UserGroupRequestDto userGroupRequestDto) {
		try {
			UserGroupEntity userGroupEntity = userGroupMapper.toEntity(userGroupRequestDto);
			UserGroupPermissions userGroupPermissions = userGroupRequestDto.getUserGroupPermissions();
			String userPermissionString = Serialization.serializeToString(userGroupPermissions);
			userGroupEntity.setUserGroupPermissionsRaw(userPermissionString);
			userGroupRepository.save(userGroupEntity);
		} catch(JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<ApplicationPermissionDto> getListApplicationPermissions() {
		List<ApplicationPermission> applicationPermissions = ApplicationPermission.getApplicationPermissions();
		return applicationPermissions.stream().map(item -> ApplicationPermissionDto.builder()
				.applicationId(item.getApplicationId())
				.actionPermissions(item.getActionPermissions())
				.build()).toList();
	}
}
