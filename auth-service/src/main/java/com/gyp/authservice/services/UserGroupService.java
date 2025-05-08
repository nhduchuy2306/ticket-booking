package com.gyp.authservice.services;

import java.util.List;

import com.gyp.authservice.dtos.usergroup.UserGroupRequestDto;
import com.gyp.authservice.dtos.usergroup.UserGroupResponseDto;

public interface UserGroupService {
	List<UserGroupResponseDto> getListUserGroups();
	UserGroupResponseDto getUserGroupById(String id);
	void createUserGroup(UserGroupRequestDto userGroupRequestDto);
}
