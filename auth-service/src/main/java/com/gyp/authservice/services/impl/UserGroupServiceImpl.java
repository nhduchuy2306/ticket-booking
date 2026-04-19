package com.gyp.authservice.services.impl;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gyp.authservice.dtos.usergroup.ApplicationPermissionDto;
import com.gyp.authservice.dtos.usergroup.UserGroupPermissions;
import com.gyp.authservice.dtos.usergroup.UserGroupRequestDto;
import com.gyp.authservice.dtos.usergroup.UserGroupResponseDto;
import com.gyp.authservice.entities.UserAccountEntity;
import com.gyp.authservice.entities.UserGroupEntity;
import com.gyp.authservice.exceptions.ReferenceException;
import com.gyp.authservice.mappers.UserGroupMapper;
import com.gyp.authservice.repositories.UserGroupRepository;
import com.gyp.authservice.services.AuthRedisCacheService;
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
	private static final Duration USER_GROUP_TTL = Duration.ofMinutes(5);
	private static final Duration USER_GROUP_ACTION_TTL = Duration.ofHours(6);
	private static final String USER_GROUP_ACTIONS_KEY = "auth:usergroup:actions";
	private static final String USER_GROUP_LIST_KEY_PREFIX = "auth:usergroup:list:";
	private static final String USER_GROUP_KEY_PREFIX = "auth:usergroup:";

	private final UserGroupRepository userGroupRepository;
	private final UserGroupMapper userGroupMapper;
	private final AuthRedisCacheService authRedisCacheService;

	@Override
	public List<UserGroupResponseDto> getListUserGroups(UserGroupSearchCriteria userGroupSearchCriteria,
			PaginatedDto paginatedDto) {
		String cacheKey = userGroupListKey(userGroupSearchCriteria, paginatedDto);
		List<UserGroupResponseDto> cachedUserGroups = authRedisCacheService.get(cacheKey,
				new TypeReference<>() {});
		if(cachedUserGroups != null) {
			return cachedUserGroups;
		}
		Specification<UserGroupEntity> specification =
				UserGroupSpecification.createSearchUserGroupSpecification(userGroupSearchCriteria);
		Pageable pageable = PageRequest.of(paginatedDto.getPage(), paginatedDto.getSize());
		Page<UserGroupEntity> userGroupEntities = userGroupRepository.findAll(specification, pageable);
		List<UserGroupResponseDto> userGroups = userGroupEntities.getContent().stream().map(userGroupMapper::toDto)
				.toList();
		authRedisCacheService.put(cacheKey, userGroups, USER_GROUP_TTL);
		return userGroups;
	}

	@Override
	public UserGroupResponseDto getUserGroupById(String id) {
		String organizationId = SecurityUtils.getCurrentOrganizationId();
		String cacheKey = userGroupKey(organizationId, id);
		UserGroupResponseDto cachedUserGroup = authRedisCacheService.get(cacheKey, UserGroupResponseDto.class);
		if(cachedUserGroup != null) {
			return cachedUserGroup;
		}
		UserGroupEntity userGroupEntity = userGroupRepository.findByIdAndOrganizationId(id, organizationId)
				.orElse(null);
		if(userGroupEntity != null) {
			UserGroupResponseDto responseDto = userGroupMapper.toDto(userGroupEntity);
			authRedisCacheService.put(cacheKey, responseDto, USER_GROUP_TTL);
			return responseDto;
		}
		return null;
	}

	@Override
	public UserGroupResponseDto createUserGroup(UserGroupRequestDto userGroupRequestDto) {
		String organizationId = SecurityUtils.getCurrentOrganizationId();
		userGroupRequestDto.setOrganizationId(organizationId);
		UserGroupEntity userGroupEntity = userGroupMapper.toEntity(userGroupRequestDto);
		userGroupEntity.setOrganizationId(organizationId);
		UserGroupPermissions userGroupPermissions = userGroupRequestDto.getUserGroupPermissions();
		String userPermissionString = Serialization.serializeToString(userGroupPermissions);
		userGroupEntity.setUserGroupPermissionsRaw(userPermissionString);
		userGroupRepository.save(userGroupEntity);
		evictUserGroupCaches(organizationId, userGroupEntity.getId());
		return userGroupMapper.toDto(userGroupEntity);
	}

	@Override
	public UserGroupResponseDto updateUserGroup(UserGroupRequestDto userGroupRequestDto, String id) {
		UserGroupEntity userGroupEntity = userGroupRepository.findById(id).orElse(null);
		if(userGroupEntity != null) {
			String organizationId = userGroupEntity.getOrganizationId() != null
					? userGroupEntity.getOrganizationId()
					: SecurityUtils.getCurrentOrganizationId();
			UserGroupPermissions userGroupPermissions = userGroupRequestDto.getUserGroupPermissions();
			String userPermissionString = Serialization.serializeToString(userGroupPermissions);
			userGroupEntity.setUserGroupPermissionsRaw(userPermissionString);
			userGroupEntity.setName(userGroupRequestDto.getName());
			userGroupEntity.setDescription(userGroupRequestDto.getDescription());
			userGroupEntity.setAdministrator(userGroupRequestDto.getAdministrator());
			userGroupEntity.setOrganizationId(organizationId);
			userGroupRepository.save(userGroupEntity);
			evictUserGroupCaches(organizationId, id);
		}
		return userGroupMapper.toDto(userGroupEntity);
	}

	@Override
	public UserGroupResponseDto deleteUserGroup(String id) {
		UserGroupEntity userGroupEntity = userGroupRepository.findById(id).orElse(null);
		if(userGroupEntity != null) {
			String organizationId = userGroupEntity.getOrganizationId() != null
					? userGroupEntity.getOrganizationId()
					: SecurityUtils.getCurrentOrganizationId();
			var userAccountList = userGroupEntity.getUserAccountEntityList();
			if(!userAccountList.isEmpty()) {
				String uuidList = userAccountList.stream().map(UserAccountEntity::getId).
						collect(Collectors.joining(","));
				throw new ReferenceException("User group is associated with: " + uuidList);
			} else {
				userGroupRepository.delete(userGroupEntity);
				evictUserGroupCaches(organizationId, id);
			}
		}

		return userGroupMapper.toDto(userGroupEntity);
	}

	@Override
	public List<ApplicationPermissionDto> getListApplicationPermissions() {
		List<ApplicationPermissionDto> cachedApplicationPermissions = authRedisCacheService.get(USER_GROUP_ACTIONS_KEY,
				new TypeReference<>() {});
		if(cachedApplicationPermissions != null) {
			return cachedApplicationPermissions;
		}
		List<ApplicationPermission> applicationPermissions = ApplicationPermission.getApplicationPermissions(false);
		List<ApplicationPermissionDto> response = applicationPermissions.stream()
				.map(item -> ApplicationPermissionDto.builder()
						.name(item.getName())
						.applicationId(item.getApplicationId())
						.actionPermissions(item.getActionPermissions())
						.build())
				.toList();
		authRedisCacheService.put(USER_GROUP_ACTIONS_KEY, response, USER_GROUP_ACTION_TTL);
		return response;
	}

	private String userGroupListKey(UserGroupSearchCriteria searchCriteria, PaginatedDto paginatedDto) {
		String organizationId = searchCriteria != null ? searchCriteria.getOrganizationId() : "unknown";
		String sortBy =
				searchCriteria != null && searchCriteria.getSortBy() != null ? searchCriteria.getSortBy() : "default";
		return USER_GROUP_LIST_KEY_PREFIX + organizationId + ":" + sortBy + ":" + paginatedDto.getPage() + ":"
				+ paginatedDto.getSize();
	}

	private String userGroupKey(String organizationId, String id) {
		return USER_GROUP_KEY_PREFIX + organizationId + ":" + id;
	}

	private void evictUserGroupCaches(String organizationId, String id) {
		authRedisCacheService.evict(userGroupKey(organizationId, id));
		authRedisCacheService.evictByPrefix(USER_GROUP_LIST_KEY_PREFIX + organizationId + ":");
	}
}
