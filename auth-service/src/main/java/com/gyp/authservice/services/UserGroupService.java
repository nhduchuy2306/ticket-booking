package com.gyp.authservice.services;

import java.util.List;

import com.gyp.authservice.dtos.usergroup.ApplicationPermissionDto;
import com.gyp.authservice.dtos.usergroup.UserGroupRequestDto;
import com.gyp.authservice.dtos.usergroup.UserGroupResponseDto;
import com.gyp.authservice.services.criteria.UserGroupSearchCriteria;
import com.gyp.common.dtos.pagination.PaginatedDto;

public interface UserGroupService {
	List<UserGroupResponseDto> getListUserGroups(UserGroupSearchCriteria userGroupSearchCriteria,
			PaginatedDto paginatedDto);

	UserGroupResponseDto getUserGroupById(String id);

	UserGroupResponseDto createUserGroup(UserGroupRequestDto userGroupRequestDto);

	UserGroupResponseDto updateUserGroup(UserGroupRequestDto userGroupRequestDto, String id);

	UserGroupResponseDto deleteUserGroup(String id);

	List<ApplicationPermissionDto> getListApplicationPermissions();
}
