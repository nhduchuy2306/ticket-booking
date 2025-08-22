package com.gyp.authservice.controllers;

import com.gyp.authservice.dtos.auth.LoginRequestDto;
import com.gyp.authservice.dtos.auth.LoginResponseDto;
import com.gyp.authservice.dtos.auth.RegisterRequestDto;
import com.gyp.authservice.dtos.auth.TokenRequestDto;
import com.gyp.authservice.dtos.auth.TokenResponse;
import com.gyp.authservice.dtos.useraccount.UserAccountResponseDto;
import com.gyp.authservice.services.AuthService;
import com.gyp.common.controllers.AbstractController;
import com.gyp.common.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController extends AbstractController {
	private static final String LOGIN_PATH = "login";
	private static final String REGISTER_PATH = "register";
	private static final String TOKEN_PATH = "token";
	private static final String REFRESH_TOKEN_PATH = "refresh-token";

	private final AuthService authService;

	@PostMapping(LOGIN_PATH)
	public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto) {
		LoginResponseDto loginResponseDto = authService.login(loginRequestDto);
		if(loginResponseDto == null) {
			return ResponseEntity.badRequest().body("Invalid username or password");
		}
		return ResponseEntity.ok(loginResponseDto);
	}

	@PostMapping(REGISTER_PATH)
	public ResponseEntity<?> register(@RequestBody RegisterRequestDto registerRequestDto) {
		UserAccountResponseDto dto = authService.register(registerRequestDto);
		if(dto == null) {
			return ResponseEntity.badRequest().body("Registration failed");
		}
		return ResponseEntity.ok(dto);
	}

	@PostMapping(TOKEN_PATH)
	public ResponseEntity<?> exchangeToken(@RequestBody TokenRequestDto request) {
		try {
			TokenResponse tokenResponse = authService.exchangeCodeForToken(request);
			if(tokenResponse == null) {
				return ResponseEntity.badRequest().body("Invalid token request");
			}
			return ResponseEntity.ok(tokenResponse);
		} catch(Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping(REFRESH_TOKEN_PATH)
	public ResponseEntity<?> refreshToken() {
		try {
			String userId = SecurityUtils.getCurrentUserId();
			TokenResponse tokenResponse = authService.refreshToken(userId);
			if(tokenResponse == null) {
				return ResponseEntity.badRequest().body("Invalid refresh token request");
			}
			return ResponseEntity.ok(tokenResponse);
		} catch(Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}
}
