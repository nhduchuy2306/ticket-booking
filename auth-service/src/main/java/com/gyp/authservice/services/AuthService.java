package com.gyp.authservice.services;

import com.gyp.authservice.dtos.auth.LoginRequestDto;
import com.gyp.authservice.dtos.auth.LoginResponseDto;

public interface AuthService {
	LoginResponseDto login(LoginRequestDto loginRequestDto);
}
