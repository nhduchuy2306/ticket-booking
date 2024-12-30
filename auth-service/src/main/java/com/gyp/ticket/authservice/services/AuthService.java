package com.gyp.ticket.authservice.services;

import com.gyp.ticket.authservice.dtos.auth.LoginRequestDto;
import com.gyp.ticket.authservice.dtos.auth.LoginResponseDto;

public interface AuthService {
	LoginResponseDto login(LoginRequestDto loginRequestDto);
}
