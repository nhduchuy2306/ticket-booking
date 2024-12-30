package com.example.ticket.authservice.services;

import com.example.ticket.authservice.dtos.auth.LoginRequestDto;
import com.example.ticket.authservice.dtos.auth.LoginResponseDto;

public interface AuthService {
	LoginResponseDto login(LoginRequestDto loginRequestDto);
}
