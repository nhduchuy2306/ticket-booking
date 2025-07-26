package com.gyp.authservice.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.gyp.authservice.dtos.useraccount.UserAccountRequestDto;
import com.gyp.authservice.dtos.useraccount.UserAccountResponseDto;
import com.gyp.authservice.entities.UserAccountEntity;
import com.gyp.authservice.entities.UserGroupEntity;
import com.gyp.authservice.mappers.UserAccountMapper;
import com.gyp.authservice.repositories.UserAccountRepository;
import com.gyp.authservice.repositories.UserGroupRepository;
import com.gyp.authservice.services.UserAccountService;
import com.gyp.authservice.services.criteria.UserAccountSearchCriteria;
import com.gyp.authservice.services.specifications.UserAccountSpecification;
import com.gyp.common.dtos.pagination.PaginatedDto;
import com.gyp.common.models.UserAccountEventModel;
import com.gyp.common.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
	public List<UserAccountResponseDto> getUserAccountList(UserAccountSearchCriteria userAccountSearchCriteria,
			PaginatedDto paginatedDto) {
		Specification<UserAccountEntity> specification =
				UserAccountSpecification.createUserAccountSpecification(userAccountSearchCriteria);
		Pageable pageable = PageRequest.of(paginatedDto.getPage(), paginatedDto.getSize());
		var userAccounts = userAccountRepository.findAll(specification, pageable);

		return userAccounts.getContent().stream()
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
		String organizationId = SecurityUtils.getCurrentOrganizationId();
		request.setOrganizationId(organizationId);
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
