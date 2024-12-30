package com.gyp.ticket.authservice.services.impl;

import com.gyp.ticket.authservice.dtos.auth.LoginRequestDto;
import com.gyp.ticket.authservice.dtos.auth.LoginResponseDto;
import com.gyp.ticket.authservice.dtos.useraccount.UserAccountResponseDto;
import com.gyp.ticket.authservice.entities.UserAccountEntity;
import com.gyp.ticket.authservice.mappers.UserAccountMapper;
import com.gyp.ticket.authservice.repositories.UserAccountRepository;
import com.gyp.ticket.authservice.services.AuthService;
import com.gyp.ticket.authservice.services.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
	private final UserAccountRepository userAccountRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenProvider jwtTokenProvider;

	@Override
	public LoginResponseDto login(LoginRequestDto loginRequestDto) {
		UserAccountEntity userAccountEntity = userAccountRepository.findByUsername(loginRequestDto.getUsername())
				.orElse(null);
		if(userAccountEntity != null) {
			String password = userAccountEntity.getPassword();
			if(passwordEncoder.matches(loginRequestDto.getPassword(), password)) {
				UserAccountResponseDto dto = UserAccountMapper.INSTANCE.toDto(userAccountEntity);
				String token = jwtTokenProvider.generateToken(dto);
				return LoginResponseDto.builder().token(token).build();
			}
		}
		return null;
	}
}
