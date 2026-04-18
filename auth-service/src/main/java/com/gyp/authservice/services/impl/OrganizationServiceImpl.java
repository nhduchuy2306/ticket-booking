package com.gyp.authservice.services.impl;

import java.time.Duration;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gyp.authservice.dtos.organization.OrganizationRequestDto;
import com.gyp.authservice.dtos.organization.OrganizationResponseDto;
import com.gyp.authservice.entities.OrganizationEntity;
import com.gyp.authservice.mappers.OrganizationMapper;
import com.gyp.authservice.repositories.OrganizationRepository;
import com.gyp.authservice.services.AuthRedisCacheService;
import com.gyp.authservice.services.OrganizationService;
import com.gyp.common.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {
	private static final Duration ORGANIZATION_TTL = Duration.ofMinutes(10);
	private static final String ORGANIZATION_LIST_KEY = "auth:organization:list";
	private static final String ORGANIZATION_KEY_PREFIX = "auth:organization:";

	private final OrganizationRepository organizationRepository;
	private final OrganizationMapper organizationMapper;
	private final AuthRedisCacheService authRedisCacheService;

	@Override
	public List<OrganizationResponseDto> getAllOrganizations() {
		List<OrganizationResponseDto> cachedOrganizations = authRedisCacheService.get(ORGANIZATION_LIST_KEY,
				new TypeReference<>() {});
		if(cachedOrganizations != null) {
			return cachedOrganizations;
		}
		List<OrganizationEntity> organizationEntities = organizationRepository.findAll();
		List<OrganizationResponseDto> organizations = organizationEntities.stream()
				.map(organizationMapper::toResponseDto)
				.toList();
		authRedisCacheService.put(ORGANIZATION_LIST_KEY, organizations, ORGANIZATION_TTL);
		return organizations;
	}

	@Override
	public OrganizationResponseDto getOrganizationById(String id) {
		OrganizationResponseDto cachedOrganization = authRedisCacheService.get(organizationKey(id),
				OrganizationResponseDto.class);
		if(cachedOrganization != null) {
			return cachedOrganization;
		}
		OrganizationEntity organizationEntity = organizationRepository.findById(id).orElse(null);
		if(organizationEntity != null) {
			OrganizationResponseDto responseDto = organizationMapper.toResponseDto(organizationEntity);
			authRedisCacheService.put(organizationKey(id), responseDto, ORGANIZATION_TTL);
			return responseDto;
		}
		return null;
	}

	@Override
	public OrganizationResponseDto createOrganization(OrganizationRequestDto organizationRequestDto) {
		OrganizationEntity organizationEntity = organizationMapper.toEntity(organizationRequestDto);
		organizationEntity = organizationRepository.save(organizationEntity);
		OrganizationResponseDto responseDto = organizationMapper.toResponseDto(organizationEntity);
		authRedisCacheService.evict(ORGANIZATION_LIST_KEY);
		authRedisCacheService.put(organizationKey(responseDto.getId()), responseDto, ORGANIZATION_TTL);
		return responseDto;
	}

	@Override
	public OrganizationResponseDto updateOrganization(String id, OrganizationRequestDto organizationRequestDto) {
		OrganizationEntity organizationEntity = organizationRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Organization not found"));
		organizationMapper.updateEntityFromDto(organizationRequestDto, organizationEntity);
		organizationEntity = organizationRepository.save(organizationEntity);
		OrganizationResponseDto responseDto = organizationMapper.toResponseDto(organizationEntity);
		authRedisCacheService.evict(ORGANIZATION_LIST_KEY);
		authRedisCacheService.put(organizationKey(id), responseDto, ORGANIZATION_TTL);
		return responseDto;
	}

	@Override
	public void deleteOrganization(String id) {
		if(!organizationRepository.existsById(id)) {
			throw new ResourceNotFoundException("Organization not found");
		}
		organizationRepository.deleteById(id);
		authRedisCacheService.evict(ORGANIZATION_LIST_KEY);
		authRedisCacheService.evict(organizationKey(id));
	}

	private String organizationKey(String id) {
		return ORGANIZATION_KEY_PREFIX + id;
	}
}
