package com.gyp.authservice.services;

import java.util.List;

import com.gyp.common.models.UserAccountModel;
import com.gyp.authservice.dtos.useraccount.UserAccountResponseDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserAccountService extends UserDetailsService {
	List<UserAccountResponseDto> getUserAccountList();
	UserAccountResponseDto getUserAccountByUserName(String username);
	UserAccountResponseDto getUserAccountById(String id);
	List<UserAccountModel> getOrganizerAccounts();
}
