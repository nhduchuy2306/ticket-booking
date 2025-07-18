package com.gyp.authservice.services.impl;

import java.util.List;

import com.gyp.authservice.dtos.organization.OrganizationRequestDto;
import com.gyp.authservice.dtos.organization.OrganizationResponseDto;
import com.gyp.authservice.entities.OrganizationEntity;
import com.gyp.authservice.mappers.OrganizationMapper;
import com.gyp.authservice.repositories.OrganizationRepository;
import com.gyp.authservice.services.OrganizationService;
import com.gyp.common.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {
	private final OrganizationRepository organizationRepository;
	private final OrganizationMapper organizationMapper;

	@Override
	public List<OrganizationResponseDto> getAllOrganizations() {
		List<OrganizationEntity> organizationEntities = organizationRepository.findAll();
		return organizationEntities.stream()
				.map(organizationMapper::toResponseDto)
				.toList();
	}

	@Override
	public OrganizationResponseDto getOrganizationById(String id) {
		OrganizationEntity organizationEntity = organizationRepository.findById(id).orElse(null);
		if(organizationEntity != null) {
			return organizationMapper.toResponseDto(organizationEntity);
		}
		return null;
	}

	@Override
	public OrganizationResponseDto createOrganization(OrganizationRequestDto organizationRequestDto) {
		OrganizationEntity organizationEntity = organizationMapper.toEntity(organizationRequestDto);
		organizationEntity = organizationRepository.save(organizationEntity);
		return organizationMapper.toResponseDto(organizationEntity);
	}

	@Override
	public OrganizationResponseDto updateOrganization(String id, OrganizationRequestDto organizationRequestDto) {
		OrganizationEntity organizationEntity = organizationRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Organization not found"));
		organizationMapper.updateEntityFromDto(organizationRequestDto, organizationEntity);
		organizationEntity = organizationRepository.save(organizationEntity);
		return organizationMapper.toResponseDto(organizationEntity);
	}

	@Override
	public void deleteOrganization(String id) {
		if(!organizationRepository.existsById(id)) {
			throw new ResourceNotFoundException("Organization not found");
		}
		organizationRepository.deleteById(id);
	}
}
