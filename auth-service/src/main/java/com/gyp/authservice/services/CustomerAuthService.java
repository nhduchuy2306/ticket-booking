package com.gyp.authservice.services;

import java.util.List;

import com.gyp.authservice.dtos.customer.CustomerAuthResponseDto;
import com.gyp.authservice.dtos.customer.CustomerLoginRequestDto;
import com.gyp.authservice.dtos.customer.CustomerRefreshTokenRequestDto;
import com.gyp.authservice.dtos.customer.CustomerRegisterRequestDto;
import com.gyp.authservice.dtos.customer.CustomerRegisterResponseDto;
import com.gyp.authservice.dtos.customerauth.CustomerResponseDto;
import com.gyp.authservice.entities.CustomerEntity;

public interface CustomerAuthService {
	CustomerRegisterResponseDto register(CustomerRegisterRequestDto registerRequestDto);

	CustomerAuthResponseDto login(CustomerLoginRequestDto loginRequestDto);

	CustomerAuthResponseDto refreshToken(CustomerRefreshTokenRequestDto refreshTokenRequestDto);

	CustomerAuthResponseDto issueTokens(CustomerEntity customerEntity);

	CustomerResponseDto getCurrentCustomer();

	List<CustomerResponseDto> getAllCustomers();
}