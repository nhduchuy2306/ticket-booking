package com.example.ticket.authservice.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.example.ticket.authservice.dtos.useraccount.UserAccountResponseDto;
import com.example.ticket.authservice.entities.CustomUserDetails;
import com.example.ticket.authservice.entities.UserAccountEntity;
import com.example.ticket.authservice.mappers.UserAccountMapper;
import com.example.ticket.authservice.repositories.UserAccountRepository;
import com.example.ticket.authservice.services.UserAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserAccountServiceImpl implements UserAccountService {
	private final UserAccountRepository userAccountRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserAccountEntity userAccountEntity = userAccountRepository.findByUsername(username).orElse(null);
		if(userAccountEntity == null) {
			throw new UsernameNotFoundException("User not found");
		}

		return CustomUserDetails.builder()
				.username(userAccountEntity.getUsername())
				.password(userAccountEntity.getPassword())
				.build();
	}

	@Override
	public List<UserAccountResponseDto> getUserAccountList() {
		return userAccountRepository.findAll().stream()
				.map(UserAccountMapper.INSTANCE::toDto)
				.collect(Collectors.toList());
	}

	@Override
	public UserAccountResponseDto getUserAccountByUserName(String username) {
		UserAccountEntity userAccountEntity = userAccountRepository.findByUsername(username).orElse(null);
		if(userAccountEntity != null) {
			return UserAccountMapper.INSTANCE.toDto(userAccountEntity);
		}
		return null;
	}
}
