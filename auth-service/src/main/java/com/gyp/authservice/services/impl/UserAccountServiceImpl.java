package com.gyp.authservice.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.gyp.authservice.dtos.useraccount.UserAccountRequestDto;
import com.gyp.authservice.dtos.useraccount.UserAccountResponseDto;
import com.gyp.authservice.entities.CustomUserDetails;
import com.gyp.authservice.entities.UserAccountEntity;
import com.gyp.authservice.entities.UserGroupEntity;
import com.gyp.authservice.mappers.UserAccountMapper;
import com.gyp.authservice.repositories.UserAccountRepository;
import com.gyp.authservice.repositories.UserGroupRepository;
import com.gyp.authservice.services.UserAccountService;
import com.gyp.common.models.UserAccountEventModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserAccountServiceImpl implements UserAccountService {
	private final UserAccountRepository userAccountRepository;
	private final UserGroupRepository userGroupRepository;
	private final UserAccountMapper userAccountMapper;

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
				.map(userAccountMapper::toResponseDto)
				.collect(Collectors.toList());
	}

	@Override
	public UserAccountResponseDto getUserAccountByUserName(String username) {
		UserAccountEntity userAccountEntity = userAccountRepository.findByUsername(username).orElse(null);
		if(userAccountEntity != null) {
			return userAccountMapper.toResponseDto(userAccountEntity);
		}
		return null;
	}

	@Override
	public UserAccountResponseDto getUserAccountById(String id) {
		UserAccountEntity userAccountEntity = userAccountRepository.findById(id).orElse(null);
		if(userAccountEntity != null) {
			return userAccountMapper.toResponseDto(userAccountEntity);
		}
		return null;
	}

	@Override
	public UserAccountResponseDto createUserAccount(UserAccountRequestDto request) {
		if(request.getUserGroupList() == null || request.getUserGroupList().isEmpty()) {
			throw new IllegalArgumentException("User group list cannot be empty");
		}
		List<UserGroupEntity> userGroupIds = userGroupRepository.findAllById(request.getUserGroupList());
		UserAccountEntity userAccountEntity = userAccountMapper.toEntity(request);
		userAccountEntity.setUserGroupEntityList(userGroupIds);
		userAccountEntity = userAccountRepository.save(userAccountEntity);
		return userAccountMapper.toResponseDto(userAccountEntity);
	}

	@Override
	public List<UserAccountEventModel> getOrganizerAccounts() {
		List<UserAccountEntity> userAccountEntityList = userAccountRepository.findAllWithAppEventCrudPermissions();
		return userAccountEntityList.stream().map(userAccountMapper::toModel).collect(Collectors.toList());
	}

	@Override
	public UserAccountResponseDto updateUserAccount(String id, UserAccountRequestDto request) {
		UserAccountEntity userAccountEntity = userAccountRepository.findById(id).orElse(null);
		if(userAccountEntity != null) {
			if(request.getUserGroupList() == null || request.getUserGroupList().isEmpty()) {
				throw new IllegalArgumentException("User group list cannot be empty");
			}
			List<UserGroupEntity> userGroupIds = userGroupRepository.findAllById(request.getUserGroupList());
			userAccountEntity.setUserGroupEntityList(userGroupIds);
			userAccountEntity = userAccountRepository.save(userAccountEntity);
			return userAccountMapper.toResponseDto(userAccountEntity);
		}
		return null;
	}

	@Override
	public UserAccountResponseDto deleteUserAccount(String id) {
		UserAccountEntity userAccountEntity = userAccountRepository.findById(id).orElse(null);
		if(userAccountEntity != null) {
			userAccountRepository.delete(userAccountEntity);
			return userAccountMapper.toResponseDto(userAccountEntity);
		}
		return null;
	}
}
