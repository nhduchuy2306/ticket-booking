package com.gyp.authservice.services;

import com.gyp.authservice.dtos.useraccount.UserAccountResponseDto;

public interface IamService {
	UserAccountResponseDto validateUser(String username, String password);

	String generateAuthCode(UserAccountResponseDto user, String clientId);
}
