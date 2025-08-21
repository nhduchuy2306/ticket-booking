package com.gyp.authservice.services.impl;

import com.gyp.authservice.dtos.auth.LoginRequestDto;
import com.gyp.authservice.dtos.auth.LoginResponseDto;
import com.gyp.authservice.dtos.auth.RegisterRequestDto;
import com.gyp.authservice.dtos.auth.TokenRequestDto;
import com.gyp.authservice.dtos.auth.TokenResponse;
import com.gyp.authservice.dtos.useraccount.UserAccountResponseDto;
import com.gyp.authservice.entities.UserAccountEntity;
import com.gyp.authservice.mappers.UserAccountMapper;
import com.gyp.authservice.repositories.UserAccountRepository;
import com.gyp.authservice.services.AuthService;
import com.gyp.authservice.services.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
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
				String token = jwtTokenProvider.generateTokenWithPermissions(dto);
				return LoginResponseDto.builder()
						.token(token)
						.userId(dto.getId())
						.username(dto.getUsername())
						.organizationId(dto.getOrganizationId())
						.build();
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

	@Override
	public TokenResponse exchangeCodeForToken(TokenRequestDto request) {
		try {
			String authCode = (String)jwtTokenProvider.getClaim(request.getCode(), "type");
			String clientId = (String)jwtTokenProvider.getClaim(request.getCode(), "clientId");

			if(!JwtTokenProvider.AUTH_CODE.equals(authCode)) {
				log.error("Invalid auth code type: {}", authCode);
				return null;
			}

			if(!request.getClientId().equals(clientId)) {
				log.error("Client ID mismatch: expected {}, got {}", request.getClientId(), clientId);
				return null;
			}

			String userAccountId = (String)jwtTokenProvider.getClaim(request.getCode(), "sub");
			UserAccountEntity userAccountEntity = userAccountRepository.findById(userAccountId)
					.orElse(null);

			if(userAccountEntity == null) {
				log.error("User account not found for ID: {}", userAccountId);
				return null;
			}

			UserAccountResponseDto userAccountResponseDto = userAccountMapper.toResponseDto(userAccountEntity);
			String token = jwtTokenProvider.generateTokenWithPermissions(userAccountResponseDto);
			return TokenResponse.builder()
					.token(token)
					.userId(userAccountResponseDto.getId())
					.name(userAccountResponseDto.getName())
					.organizationId(userAccountResponseDto.getOrganizationId())
					.build();
		} catch(Exception e) {
			log.error("Error exchanging code for token: {}", e.getMessage());
			return null;
		}
	}
}
