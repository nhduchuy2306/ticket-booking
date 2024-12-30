package com.example.ticket.authservice.controllers;

import java.util.Objects;

import com.example.ticket.authservice.dtos.auth.LoginRequestDto;
import com.example.ticket.authservice.dtos.auth.LoginResponseDto;
import com.example.ticket.authservice.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(AuthController.AUTH_CONTROLLER_PATH)
public class AuthController {
	public static final String AUTH_CONTROLLER_PATH = "auths";

	private static final String LOGIN_PATH = "login";
	private static final String LOGOUT_PATH = "logout";

	private final AuthService authService;

	@PostMapping(AuthController.LOGIN_PATH)
	public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto) {
		LoginResponseDto loginResponseDto = authService.login(loginRequestDto);
		return ResponseEntity.ok(Objects.requireNonNullElse(loginResponseDto, "Invalid username or password"));
	}

	@PostMapping(AuthController.LOGOUT_PATH)
	public ResponseEntity<?> logout() {
		return ResponseEntity.ok("Logout");
	}
}
