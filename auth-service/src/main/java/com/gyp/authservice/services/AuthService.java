package com.gyp.authservice.services;

import com.gyp.authservice.dtos.auth.LoginRequestDto;
import com.gyp.authservice.dtos.auth.LoginResponseDto;
import com.gyp.authservice.dtos.auth.RegisterRequestDto;
import com.gyp.authservice.dtos.useraccount.UserAccountResponseDto;

public interface AuthService {
	LoginResponseDto login(LoginRequestDto loginRequestDto);
	UserAccountResponseDto register(RegisterRequestDto registerRequestDto);

}
