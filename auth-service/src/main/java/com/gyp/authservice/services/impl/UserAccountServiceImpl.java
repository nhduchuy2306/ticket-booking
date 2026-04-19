package com.gyp.authservice.services.impl;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gyp.authservice.dtos.useraccount.UserAccountRequestDto;
import com.gyp.authservice.dtos.useraccount.UserAccountResponseDto;
import com.gyp.authservice.entities.UserAccountEntity;
import com.gyp.authservice.entities.UserGroupEntity;
import com.gyp.authservice.mappers.UserAccountMapper;
import com.gyp.authservice.repositories.UserAccountRepository;
import com.gyp.authservice.repositories.UserGroupRepository;
import com.gyp.authservice.services.AuthRedisCacheService;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserAccountServiceImpl implements UserAccountService {
	private static final Duration USER_ACCOUNT_TTL = Duration.ofMinutes(5);
	private static final Duration ORGANIZER_ACCOUNT_TTL = Duration.ofMinutes(5);
	private static final String USER_ACCOUNT_LIST_KEY_PREFIX = "auth:useraccount:list:";
	private static final String USER_ACCOUNT_KEY_PREFIX = "auth:useraccount:";
	private static final String USER_ACCOUNT_USERNAME_KEY_PREFIX = "auth:useraccount:username:";
	private static final String USER_ACCOUNT_BY_GROUP_KEY_PREFIX = "auth:useraccount:by-group:";
	private static final String ORGANIZER_ACCOUNTS_KEY = "auth:useraccount:organizers";

	private final UserAccountRepository userAccountRepository;
	private final UserGroupRepository userGroupRepository;
	private final UserAccountMapper userAccountMapper;
	private final PasswordEncoder passwordEncoder;
	private final AuthRedisCacheService authRedisCacheService;

	@Override
	public List<UserAccountResponseDto> getUserAccountList(UserAccountSearchCriteria userAccountSearchCriteria,
			PaginatedDto paginatedDto) {
		String cacheKey = userAccountListKey(userAccountSearchCriteria, paginatedDto);
		List<UserAccountResponseDto> cachedUserAccounts = authRedisCacheService.get(cacheKey,
				new TypeReference<>() {});
		if(cachedUserAccounts != null) {
			return cachedUserAccounts;
		}
		Specification<UserAccountEntity> specification =
				UserAccountSpecification.createUserAccountSpecification(userAccountSearchCriteria);
		Pageable pageable = PageRequest.of(paginatedDto.getPage(), paginatedDto.getSize());
		var userAccounts = userAccountRepository.findAll(specification, pageable);
		List<UserAccountResponseDto> response = userAccounts.getContent().stream()
				.map(userAccountMapper::toResponseDto)
				.collect(Collectors.toList());
		authRedisCacheService.put(cacheKey, response, USER_ACCOUNT_TTL);
		return response;
	}

	@Override
	public UserAccountResponseDto getUserAccountByUserName(String username) {
		UserAccountResponseDto cachedUserAccount = authRedisCacheService.get(userAccountUsernameKey(username),
				UserAccountResponseDto.class);
		if(cachedUserAccount != null) {
			return cachedUserAccount;
		}
		UserAccountEntity userAccountEntity = userAccountRepository.findByUsername(username).orElse(null);
		if(userAccountEntity != null) {
			UserAccountResponseDto responseDto = userAccountMapper.toResponseDto(userAccountEntity);
			authRedisCacheService.put(userAccountUsernameKey(username), responseDto, USER_ACCOUNT_TTL);
			authRedisCacheService.put(userAccountKey(userAccountEntity.getId()), responseDto, USER_ACCOUNT_TTL);
			return responseDto;
		}
		return null;
	}

	@Override
	public UserAccountResponseDto getUserAccountById(String id) {
		UserAccountResponseDto cachedUserAccount = authRedisCacheService.get(userAccountKey(id),
				UserAccountResponseDto.class);
		if(cachedUserAccount != null) {
			return cachedUserAccount;
		}
		UserAccountEntity userAccountEntity = userAccountRepository.findById(id).orElse(null);
		if(userAccountEntity != null) {
			UserAccountResponseDto responseDto = userAccountMapper.toResponseDto(userAccountEntity);
			authRedisCacheService.put(userAccountKey(id), responseDto, USER_ACCOUNT_TTL);
			authRedisCacheService.put(userAccountUsernameKey(userAccountEntity.getUsername()), responseDto,
					USER_ACCOUNT_TTL);
			return responseDto;
		}
		return null;
	}

	@Override
	public UserAccountResponseDto getUserAccountByUserGroupId(String userGroupId, String userAccountId) {
		String cacheKey = userAccountByGroupKey(userGroupId, userAccountId);
		UserAccountResponseDto cachedUserAccount = authRedisCacheService.get(cacheKey, UserAccountResponseDto.class);
		if(cachedUserAccount != null) {
			return cachedUserAccount;
		}
		Optional<UserAccountEntity> userAccountEntity = userAccountRepository.findByUserGroupId(userGroupId,
				userAccountId);
		UserAccountEntity userAccount = userAccountEntity.orElse(null);
		UserAccountResponseDto responseDto = userAccount != null ? userAccountMapper.toResponseDto(userAccount) : null;
		if(responseDto != null) {
			authRedisCacheService.put(cacheKey, responseDto, USER_ACCOUNT_TTL);
		}
		return responseDto;
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
		userAccountEntity.setOrganizationId(organizationId);
		userAccountEntity.setPassword(passwordEncoder.encode(userAccountEntity.getPassword()));
		userAccountEntity.setUserGroupEntityList(userGroupIds);
		userAccountEntity = userAccountRepository.save(userAccountEntity);
		UserAccountResponseDto responseDto = userAccountMapper.toResponseDto(userAccountEntity);
		evictUserAccountCaches(request.getOrganizationId(), userAccountEntity.getId(), userAccountEntity.getUsername(),
				userAccountEntity.getUsername());
		return responseDto;
	}

	@Override
	public List<UserAccountEventModel> getOrganizerAccounts() {
		List<UserAccountEventModel> cachedOrganizerAccounts = authRedisCacheService.get(ORGANIZER_ACCOUNTS_KEY,
				new TypeReference<>() {});
		if(cachedOrganizerAccounts != null) {
			return cachedOrganizerAccounts;
		}
		List<UserAccountEntity> userAccountEntityList = userAccountRepository.findAllWithAppEventCrudPermissions();
		List<UserAccountEventModel> organizerAccounts = userAccountEntityList.stream()
				.map(userAccountMapper::toModel)
				.collect(Collectors.toList());
		authRedisCacheService.put(ORGANIZER_ACCOUNTS_KEY, organizerAccounts, ORGANIZER_ACCOUNT_TTL);
		return organizerAccounts;
	}

	@Override
	public UserAccountResponseDto updateUserAccount(String id, UserAccountRequestDto request) {
		UserAccountEntity userAccountEntity = userAccountRepository.findById(id).orElse(null);
		if(userAccountEntity != null) {
			String previousUsername = userAccountEntity.getUsername();
			String organizationId = userAccountEntity.getOrganizationId() != null
					? userAccountEntity.getOrganizationId()
					: request.getOrganizationId();
			if(request.getUserGroupList() != null && !request.getUserGroupList().isEmpty()) {
				List<UserGroupEntity> userGroupIds = userGroupRepository.findAllById(request.getUserGroupList());
				userAccountEntity.setUserGroupEntityList(userGroupIds);
			}
			userAccountMapper.updateEntityFromDto(request, userAccountEntity);
			userAccountEntity.setOrganizationId(organizationId);
			userAccountEntity = userAccountRepository.save(userAccountEntity);
			UserAccountResponseDto responseDto = userAccountMapper.toResponseDto(userAccountEntity);
			evictUserAccountCaches(organizationId, userAccountEntity.getId(), previousUsername,
					userAccountEntity.getUsername());
			return responseDto;
		}
		return null;
	}

	@Override
	public UserAccountResponseDto deleteUserAccount(String id) {
		UserAccountEntity userAccountEntity = userAccountRepository.findById(id).orElse(null);
		if(userAccountEntity != null) {
			String organizationId = userAccountEntity.getOrganizationId();
			userAccountRepository.delete(userAccountEntity);
			UserAccountResponseDto responseDto = userAccountMapper.toResponseDto(userAccountEntity);
			evictUserAccountCaches(organizationId, id, userAccountEntity.getUsername(),
					userAccountEntity.getUsername());
			return responseDto;
		}
		return null;
	}

	private String userAccountListKey(UserAccountSearchCriteria searchCriteria, PaginatedDto paginatedDto) {
		String organizationId = searchCriteria != null ? searchCriteria.getOrganizationId() : "unknown";
		String sortBy =
				searchCriteria != null && searchCriteria.getSortBy() != null ? searchCriteria.getSortBy() : "default";
		return USER_ACCOUNT_LIST_KEY_PREFIX + organizationId + ":" + sortBy + ":" + paginatedDto.getPage() + ":"
				+ paginatedDto.getSize();
	}

	private String userAccountKey(String id) {
		return USER_ACCOUNT_KEY_PREFIX + id;
	}

	private String userAccountUsernameKey(String username) {
		return USER_ACCOUNT_USERNAME_KEY_PREFIX + username;
	}

	private String userAccountByGroupKey(String userGroupId, String userAccountId) {
		return USER_ACCOUNT_BY_GROUP_KEY_PREFIX + userGroupId + ":" + userAccountId;
	}

	private void evictUserAccountCaches(String organizationId, String userAccountId, String previousUsername,
			String currentUsername) {
		authRedisCacheService.evict(userAccountKey(userAccountId));
		authRedisCacheService.evict(userAccountUsernameKey(previousUsername));
		if(currentUsername != null && !currentUsername.equals(previousUsername)) {
			authRedisCacheService.evict(userAccountUsernameKey(currentUsername));
		}
		if(organizationId != null && !organizationId.isBlank()) {
			authRedisCacheService.evictByPrefix(USER_ACCOUNT_LIST_KEY_PREFIX + organizationId + ":");
		}
		authRedisCacheService.evictByPrefix(USER_ACCOUNT_BY_GROUP_KEY_PREFIX);
		authRedisCacheService.evict(ORGANIZER_ACCOUNTS_KEY);
	}
}
