package com.gyp.ticket.authservice.services.impl;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gyp.common.converters.Serialization;
import com.gyp.ticket.authservice.dtos.usergroup.UserGroupPermissions;
import com.gyp.ticket.authservice.dtos.usergroup.UserGroupRequestDto;
import com.gyp.ticket.authservice.dtos.usergroup.UserGroupResponseDto;
import com.gyp.ticket.authservice.entities.UserGroupEntity;
import com.gyp.ticket.authservice.mappers.UserGroupMapper;
import com.gyp.ticket.authservice.repositories.UserGroupRepository;
import com.gyp.ticket.authservice.services.UserGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserGroupServiceImpl implements UserGroupService {
	private final UserGroupRepository userGroupRepository;

	@Override
	public List<UserGroupResponseDto> getListUserGroups() {
		List<UserGroupEntity> userGroupEntities = userGroupRepository.findAll();
		return userGroupEntities.stream().map(UserGroupMapper.INSTANCE::toDto).toList();
	}

	@Override
	public UserGroupResponseDto getUserGroupById(String id) {
		UserGroupEntity userGroupEntity = userGroupRepository.findById(id).orElse(null);
		if(userGroupEntity != null) {
			return UserGroupMapper.INSTANCE.toDto(userGroupEntity);
		}
		return null;
	}

	@Override
	public void createUserGroup(UserGroupRequestDto userGroupRequestDto) {
		try {
			UserGroupEntity userGroupEntity = UserGroupMapper.INSTANCE.toEntity(userGroupRequestDto);
			UserGroupPermissions userGroupPermissions = userGroupRequestDto.getUserGroupPermissions();
			String userPermissionString = Serialization.serializeToString(userGroupPermissions);
			userGroupEntity.setUserGroupPermissionsRaw(userPermissionString);
			userGroupRepository.save(userGroupEntity);
		} catch(JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}
