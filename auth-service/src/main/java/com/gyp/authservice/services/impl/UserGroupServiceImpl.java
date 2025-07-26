package com.gyp.authservice.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gyp.authservice.dtos.usergroup.ApplicationPermissionDto;
import com.gyp.authservice.dtos.usergroup.UserGroupPermissions;
import com.gyp.authservice.dtos.usergroup.UserGroupRequestDto;
import com.gyp.authservice.dtos.usergroup.UserGroupResponseDto;
import com.gyp.authservice.entities.UserAccountEntity;
import com.gyp.authservice.entities.UserGroupEntity;
import com.gyp.authservice.exceptions.ReferenceException;
import com.gyp.authservice.mappers.UserGroupMapper;
import com.gyp.authservice.repositories.UserGroupRepository;
import com.gyp.authservice.services.UserGroupService;
import com.gyp.authservice.services.criteria.UserGroupSearchCriteria;
import com.gyp.authservice.services.specifications.UserGroupSpecification;
import com.gyp.common.converters.Serialization;
import com.gyp.common.dtos.pagination.PaginatedDto;
import com.gyp.common.enums.permission.ApplicationPermission;
import com.gyp.common.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserGroupServiceImpl implements UserGroupService {
	private final UserGroupRepository userGroupRepository;
	private final UserGroupMapper userGroupMapper;

	@Override
	public List<UserGroupResponseDto> getListUserGroups(UserGroupSearchCriteria userGroupSearchCriteria,
			PaginatedDto paginatedDto) {
		Specification<UserGroupEntity> specification =
				UserGroupSpecification.createSearchUserGroupSpecification(userGroupSearchCriteria);
		Pageable pageable = PageRequest.of(paginatedDto.getPage(), paginatedDto.getSize());
		Page<UserGroupEntity> userGroupEntities = userGroupRepository.findAll(specification, pageable);
		return userGroupEntities.getContent().stream().map(userGroupMapper::toDto).toList();
	}

	@Override
	public UserGroupResponseDto getUserGroupById(String id) {
		String organizationId = SecurityUtils.getCurrentOrganizationId();
		UserGroupEntity userGroupEntity = userGroupRepository.findByIdAndOrganizationEntityId(id, organizationId)
				.orElse(null);
		if(userGroupEntity != null) {
			return userGroupMapper.toDto(userGroupEntity);
		}
		return null;
	}

	@Override
	public UserGroupResponseDto createUserGroup(UserGroupRequestDto userGroupRequestDto) {
		try {
			String organizationId = SecurityUtils.getCurrentOrganizationId();
			userGroupRequestDto.setOrganizationId(organizationId);
			UserGroupEntity userGroupEntity = userGroupMapper.toEntity(userGroupRequestDto);
			UserGroupPermissions userGroupPermissions = userGroupRequestDto.getUserGroupPermissions();
			String userPermissionString = Serialization.serializeToString(userGroupPermissions);
			userGroupEntity.setUserGroupPermissionsRaw(userPermissionString);
			userGroupRepository.save(userGroupEntity);

			return userGroupMapper.toDto(userGroupEntity);
		} catch(JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public UserGroupResponseDto updateUserGroup(UserGroupRequestDto userGroupRequestDto, String id) {
		try {
			UserGroupEntity userGroupEntity = userGroupRepository.findById(id).orElse(null);
			if(userGroupEntity != null) {
				UserGroupPermissions userGroupPermissions = userGroupRequestDto.getUserGroupPermissions();
				String userPermissionString = Serialization.serializeToString(userGroupPermissions);
				userGroupEntity.setUserGroupPermissionsRaw(userPermissionString);
				userGroupEntity.setName(userGroupRequestDto.getName());
				userGroupEntity.setDescription(userGroupRequestDto.getDescription());
				userGroupEntity.setAdministrator(userGroupRequestDto.getAdministrator());
				userGroupRepository.save(userGroupEntity);
			}

			return userGroupMapper.toDto(userGroupEntity);
		} catch(JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public UserGroupResponseDto deleteUserGroup(String id) {
		UserGroupEntity userGroupEntity = userGroupRepository.findById(id).orElse(null);
		if(userGroupEntity != null) {
			var userAccountList = userGroupEntity.getUserAccountEntityList();
			if(!userAccountList.isEmpty()) {
				String uuidList = userAccountList.stream().map(UserAccountEntity::getId).
						collect(Collectors.joining(","));
				throw new ReferenceException("User group is associated with: " + uuidList);
			} else {
				userGroupRepository.delete(userGroupEntity);
			}
		}

		return userGroupMapper.toDto(userGroupEntity);
	}

	@Override
	public List<ApplicationPermissionDto> getListApplicationPermissions() {
		List<ApplicationPermission> applicationPermissions = ApplicationPermission.getApplicationPermissions(false);
		return applicationPermissions.stream().map(item -> ApplicationPermissionDto.builder()
				.name(item.getName())
				.applicationId(item.getApplicationId())
				.actionPermissions(item.getActionPermissions())
				.build()).toList();
	}
}
