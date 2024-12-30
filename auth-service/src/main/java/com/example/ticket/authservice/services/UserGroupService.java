package com.example.ticket.authservice.services;

import java.util.List;

import com.example.ticket.authservice.dtos.usergroup.UserGroupRequestDto;
import com.example.ticket.authservice.dtos.usergroup.UserGroupResponseDto;

public interface UserGroupService {
	List<UserGroupResponseDto> getListUserGroups();
	UserGroupResponseDto getUserGroupById(String id);
	void createUserGroup(UserGroupRequestDto userGroupRequestDto);
}
