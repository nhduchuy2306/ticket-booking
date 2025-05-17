package com.gyp.authservice.services.impl;

import com.gyp.authservice.dtos.auth.LoginRequestDto;
import com.gyp.authservice.dtos.auth.LoginResponseDto;
import com.gyp.authservice.dtos.auth.RegisterRequestDto;
import com.gyp.authservice.dtos.useraccount.UserAccountResponseDto;
import com.gyp.authservice.entities.UserAccountEntity;
import com.gyp.authservice.mappers.UserAccountMapper;
import com.gyp.authservice.repositories.UserAccountRepository;
import com.gyp.authservice.services.AuthService;
import com.gyp.authservice.services.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
	private final UserAccountRepository userAccountRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenProvider jwtTokenProvider;

	private final UserAccountMapper userAccountMapper;

	@Override
	public LoginResponseDto login(LoginRequestDto loginRequestDto) {
		UserAccountEntity userAccountEntity = userAccountRepository.findByUsername(loginRequestDto.getUsername())
				.orElse(null);
		if(userAccountEntity != null) {
			String password = userAccountEntity.getPassword();
			if(passwordEncoder.matches(loginRequestDto.getPassword(), password)) {
				UserAccountResponseDto dto = userAccountMapper.toResponseDto(userAccountEntity);
				String token = jwtTokenProvider.generateToken(dto);
				return LoginResponseDto.builder().token(token).build();
			}
		}
		return null;
	}

	@Override
	public UserAccountResponseDto register(RegisterRequestDto registerRequestDto) {
		if(registerRequestDto.getPassword().equals(registerRequestDto.getConfirmPassword())) {
			UserAccountEntity userAccountEntity = userAccountMapper.toEntity(registerRequestDto);
			userAccountEntity.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));
			userAccountEntity = userAccountRepository.save(userAccountEntity);
			return userAccountMapper.toResponseDto(userAccountEntity);
		}
		return null;
	}
}
