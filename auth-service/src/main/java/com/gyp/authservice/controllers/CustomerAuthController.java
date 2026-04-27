package com.gyp.authservice.controllers;

import java.util.List;

import jakarta.validation.Valid;

import com.gyp.authservice.dtos.customer.CustomerLoginRequestDto;
import com.gyp.authservice.dtos.customer.CustomerRefreshTokenRequestDto;
import com.gyp.authservice.dtos.customer.CustomerRegisterRequestDto;
import com.gyp.authservice.dtos.customerauth.CustomerResponseDto;
import com.gyp.authservice.services.CustomerAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(CustomerAuthController.CUSTOMER_AUTH_CONTROLLER_PATH)
public class CustomerAuthController {
	public static final String CUSTOMER_AUTH_CONTROLLER_PATH = "/customer/auth";

	private final CustomerAuthService customerAuthService;

	@PostMapping("/register")
	public ResponseEntity<?> register(@Valid @RequestBody CustomerRegisterRequestDto requestDto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(customerAuthService.register(requestDto));
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody CustomerLoginRequestDto requestDto) {
		return ResponseEntity.ok(customerAuthService.login(requestDto));
	}

	@PostMapping("/refresh-token")
	public ResponseEntity<?> refreshToken(@Valid @RequestBody CustomerRefreshTokenRequestDto requestDto) {
		return ResponseEntity.ok(customerAuthService.refreshToken(requestDto));
	}

	@GetMapping("/me")
	public ResponseEntity<?> me() {
		return ResponseEntity.ok(customerAuthService.getCurrentCustomer());
	}

	@GetMapping("/customers")
	public ResponseEntity<List<CustomerResponseDto>> getAllCustomers() {
		return ResponseEntity.ok(customerAuthService.getAllCustomers());
	}
}