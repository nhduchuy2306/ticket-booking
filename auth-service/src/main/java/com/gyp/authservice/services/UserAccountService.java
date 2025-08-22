package com.gyp.authservice.services;

import java.util.List;

import com.gyp.authservice.dtos.useraccount.UserAccountRequestDto;
import com.gyp.authservice.dtos.useraccount.UserAccountResponseDto;
import com.gyp.authservice.services.criteria.UserAccountSearchCriteria;
import com.gyp.common.dtos.pagination.PaginatedDto;
import com.gyp.common.models.UserAccountEventModel;

public interface UserAccountService {
	List<UserAccountResponseDto> getUserAccountList(UserAccountSearchCriteria userAccountSearchCriteria,
			PaginatedDto paginatedDto);

	UserAccountResponseDto getUserAccountByUserName(String username);

	UserAccountResponseDto getUserAccountById(String id);

	UserAccountResponseDto getUserAccountByUserGroupId(String userGroupId, String userAccountId);

	UserAccountResponseDto createUserAccount(UserAccountRequestDto request);

	List<UserAccountEventModel> getOrganizerAccounts();

	UserAccountResponseDto updateUserAccount(String id, UserAccountRequestDto request);

	UserAccountResponseDto deleteUserAccount(String id);
}
