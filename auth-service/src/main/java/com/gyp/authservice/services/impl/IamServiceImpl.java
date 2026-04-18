package com.gyp.authservice.services.impl;

import java.time.Duration;

import com.gyp.authservice.dtos.useraccount.UserAccountResponseDto;
import com.gyp.authservice.entities.UserAccountEntity;
import com.gyp.authservice.mappers.UserAccountMapper;
import com.gyp.authservice.repositories.UserAccountRepository;
import com.gyp.authservice.services.AuthRedisCacheService;
import com.gyp.authservice.services.IamService;
import com.gyp.authservice.services.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IamServiceImpl implements IamService {
	private static final Duration USER_ACCOUNT_TTL = Duration.ofMinutes(5);
	private static final String USER_ACCOUNT_USERNAME_KEY_PREFIX = "auth:useraccount:username:";

	private final UserAccountRepository userAccountRepository;
	private final PasswordEncoder passwordEncoder;
	private final UserAccountMapper userAccountMapper;
	private final JwtTokenProvider jwtTokenProvider;
	private final AuthRedisCacheService authRedisCacheService;

	@Override
	public UserAccountResponseDto validateUser(String username, String password) {
		UserAccountResponseDto cachedUserAccount = authRedisCacheService.get(
				USER_ACCOUNT_USERNAME_KEY_PREFIX + username, UserAccountResponseDto.class);
		if(cachedUserAccount != null) {
			UserAccountEntity cachedEntity = userAccountRepository.findByUsername(username).orElse(null);
			if(cachedEntity != null && passwordEncoder.matches(password, cachedEntity.getPassword())) {
				return cachedUserAccount;
			}
		}
		UserAccountEntity userAccountEntity = userAccountRepository.findByUsername(username)
				.orElse(null);
		if(userAccountEntity != null) {
			String userAccountPassword = userAccountEntity.getPassword();
			if(passwordEncoder.matches(password, userAccountPassword)) {
				UserAccountResponseDto responseDto = userAccountMapper.toResponseDto(userAccountEntity);
				authRedisCacheService.put(USER_ACCOUNT_USERNAME_KEY_PREFIX + username, responseDto, USER_ACCOUNT_TTL);
				return responseDto;
			}
		}
		return null;
	}

	@Override
	public String generateAuthCode(UserAccountResponseDto user, String clientId) {
		return jwtTokenProvider.generateAuthCode(user, clientId);
	}
}
