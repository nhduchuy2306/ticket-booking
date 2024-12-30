package com.gyp.ticket.authservice.services;

import java.util.List;

import com.gyp.ticket.authservice.dtos.usergroup.UserGroupRequestDto;
import com.gyp.ticket.authservice.dtos.usergroup.UserGroupResponseDto;

public interface UserGroupService {
	List<UserGroupResponseDto> getListUserGroups();
	UserGroupResponseDto getUserGroupById(String id);
	void createUserGroup(UserGroupRequestDto userGroupRequestDto);
}
