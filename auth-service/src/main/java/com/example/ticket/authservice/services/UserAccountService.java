package com.example.ticket.authservice.services;

import java.util.List;

import com.example.ticket.authservice.dtos.useraccount.UserAccountResponseDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserAccountService extends UserDetailsService {
	List<UserAccountResponseDto> getUserAccountList();
	UserAccountResponseDto getUserAccountByUserName(String username);
}
