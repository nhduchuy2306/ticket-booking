package com.gyp.authservice.services.impl;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

import com.gyp.authservice.dtos.customer.CustomerAuthResponseDto;
import com.gyp.authservice.dtos.customer.CustomerLoginRequestDto;
import com.gyp.authservice.dtos.customer.CustomerRefreshTokenRequestDto;
import com.gyp.authservice.dtos.customer.CustomerRegisterRequestDto;
import com.gyp.authservice.dtos.customer.CustomerRegisterResponseDto;
import com.gyp.authservice.dtos.customerauth.CustomerResponseDto;
import com.gyp.authservice.entities.CustomerEntity;
import com.gyp.authservice.entities.CustomerRefreshTokenEntity;
import com.gyp.authservice.repositories.CustomerRefreshTokenRepository;
import com.gyp.authservice.repositories.CustomerRepository;
import com.gyp.authservice.services.AuthRedisCacheService;
import com.gyp.authservice.services.CustomerAuthService;
import com.gyp.authservice.services.CustomerJwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerAuthServiceImpl implements CustomerAuthService {
	private static final String LOCAL_PROVIDER = "local";
	private static final long REFRESH_TOKEN_EXPIRATION_DAYS = 7L;
	private static final Duration CUSTOMER_TTL = Duration.ofMinutes(5);
	private static final String CUSTOMER_CURRENT_KEY_PREFIX = "auth:customer:current:";
	private static final String CUSTOMER_EMAIL_KEY_PREFIX = "auth:customer:email:";

	private final CustomerRepository customerRepository;
	private final CustomerRefreshTokenRepository customerRefreshTokenRepository;
	private final PasswordEncoder passwordEncoder;
	private final CustomerJwtTokenProvider customerJwtTokenProvider;
	private final AuthRedisCacheService authRedisCacheService;

	@Override
	public CustomerRegisterResponseDto register(CustomerRegisterRequestDto registerRequestDto) {
		if(customerRepository.existsByEmail(registerRequestDto.getEmail())) {
			throw new ResponseStatusException(CONFLICT, "Email is already registered");
		}

		LocalDateTime now = LocalDateTime.now();
		CustomerEntity customerEntity = CustomerEntity.builder()
				.id(UUID.randomUUID().toString())
				.name(registerRequestDto.getName())
				.email(registerRequestDto.getEmail())
				.password(passwordEncoder.encode(registerRequestDto.getPassword()))
				.provider(LOCAL_PROVIDER)
				.build();
		customerEntity.setCreateTimestamp(now);
		customerEntity.setChangeTimestamp(now);

		customerRepository.save(customerEntity);
		authRedisCacheService.evict(customerEmailKey(customerEntity.getEmail()));
		authRedisCacheService.evict(customerCurrentKey(customerEntity.getEmail()));

		return CustomerRegisterResponseDto.builder()
				.id(customerEntity.getId())
				.email(customerEntity.getEmail())
				.message("Registration successful. You can sign in now.")
				.build();
	}

	@Override
	public CustomerAuthResponseDto login(CustomerLoginRequestDto loginRequestDto) {
		CustomerResponseDto cachedCustomer = authRedisCacheService.get(customerEmailKey(loginRequestDto.getEmail()),
				CustomerResponseDto.class);
		if(cachedCustomer != null) {
			CustomerEntity customerEntity = customerRepository.findByEmailAndProvider(loginRequestDto.getEmail(),
					LOCAL_PROVIDER).orElse(null);
			if(customerEntity != null && customerEntity.getPassword() != null && passwordEncoder.matches(
					loginRequestDto.getPassword(), customerEntity.getPassword())) {
				return issueTokens(customerEntity);
			}
		}

		CustomerEntity customerEntity = customerRepository.findByEmailAndProvider(
						loginRequestDto.getEmail(), LOCAL_PROVIDER)
				.orElseThrow(() -> new ResponseStatusException(UNAUTHORIZED, "Invalid email or password"));

		if(customerEntity.getPassword() == null || !passwordEncoder.matches(loginRequestDto.getPassword(),
				customerEntity.getPassword())) {
			throw new ResponseStatusException(UNAUTHORIZED, "Invalid email or password");
		}

		return issueTokens(customerEntity);
	}

	@Override
	public CustomerAuthResponseDto refreshToken(CustomerRefreshTokenRequestDto refreshTokenRequestDto) {
		CustomerRefreshTokenEntity refreshTokenEntity = customerRefreshTokenRepository
				.findByTokenAndRevokedFalseAndExpiryAfter(refreshTokenRequestDto.getRefreshToken(), LocalDateTime.now())
				.orElseThrow(() -> new ResponseStatusException(UNAUTHORIZED, "Invalid refresh token"));

		refreshTokenEntity.setRevoked(Boolean.TRUE);
		customerRefreshTokenRepository.save(refreshTokenEntity);

		return issueTokens(refreshTokenEntity.getCustomer());
	}

	@Override
	public CustomerAuthResponseDto issueTokens(CustomerEntity customerEntity) {
		String accessToken = customerJwtTokenProvider.generateAccessToken(customerEntity);
		String refreshToken =
				UUID.randomUUID().toString().replace("-", "") + UUID.randomUUID().toString().replace("-", "");
		LocalDateTime now = LocalDateTime.now();

		CustomerRefreshTokenEntity refreshTokenEntity = CustomerRefreshTokenEntity.builder()
				.customer(customerEntity)
				.token(refreshToken)
				.expiry(now.plusDays(REFRESH_TOKEN_EXPIRATION_DAYS))
				.revoked(Boolean.FALSE)
				.build();
		customerRefreshTokenRepository.save(refreshTokenEntity);

		return CustomerAuthResponseDto.builder()
				.accessToken(accessToken)
				.refreshToken(refreshToken)
				.expiresIn(customerJwtTokenProvider.getAccessTokenExpiresInSeconds())
				.build();
	}

	@Override
	public CustomerResponseDto getCurrentCustomer() {
		var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(principal instanceof Jwt jwt) {
			var claims = jwt.getClaims();
			String email = claims.get("email").toString();
			if(StringUtils.isNotEmpty(email)) {
				CustomerResponseDto cachedCustomer = authRedisCacheService.get(customerCurrentKey(email),
						CustomerResponseDto.class);
				if(cachedCustomer != null) {
					return cachedCustomer;
				}

				var customer = customerRepository.findByEmail(email)
						.orElseThrow(() -> new ResponseStatusException(UNAUTHORIZED, "Invalid email"));

				if(customer != null) {
					CustomerResponseDto responseDto = CustomerResponseDto.builder()
							.id(customer.getId())
							.name(customer.getName())
							.email(customer.getEmail())
							.phoneNumber(customer.getPhoneNumber())
							.dob(customer.getDob())
							.provider(customer.getProvider())
							.providerId(customer.getProviderId())
							.build();
					authRedisCacheService.put(customerCurrentKey(email), responseDto, CUSTOMER_TTL);
					authRedisCacheService.put(customerEmailKey(email), responseDto, CUSTOMER_TTL);
					return responseDto;
				}
			}
		}
		return null;
	}

	private String customerCurrentKey(String email) {
		return CUSTOMER_CURRENT_KEY_PREFIX + email;
	}

	private String customerEmailKey(String email) {
		return CUSTOMER_EMAIL_KEY_PREFIX + email;
	}
}