package com.gyp.authservice.controllers;

import java.util.Objects;

import com.gyp.authservice.dtos.auth.LoginRequestDto;
import com.gyp.authservice.dtos.auth.LoginResponseDto;
import com.gyp.authservice.services.AuthService;
import com.gyp.common.controllers.AbstractController;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController extends AbstractController {

	private static final String LOGIN_PATH = "login";
	private static final String LOGOUT_PATH = "logout";
	private static final String REGISTER_PATH = "register";

	private final AuthService authService;

	@PostMapping(AuthController.LOGIN_PATH)
	public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto) {
		LoginResponseDto loginResponseDto = authService.login(loginRequestDto);
		return createResponseOk(Objects.requireNonNullElse(loginResponseDto, "Invalid username or password"));
	}

	@PostMapping(AuthController.LOGOUT_PATH)
	public ResponseEntity<?> logout() {
		return ResponseEntity.ok("Logout");
	}

	@PostMapping(AuthController.REGISTER_PATH)
	public ResponseEntity<?> register() {
		return ResponseEntity.ok("Register");
	}
}
