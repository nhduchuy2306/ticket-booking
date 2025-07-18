package com.gyp.authservice.services;

import java.util.List;

import com.gyp.authservice.dtos.organization.OrganizationRequestDto;
import com.gyp.authservice.dtos.organization.OrganizationResponseDto;

public interface OrganizationService {
	List<OrganizationResponseDto> getAllOrganizations();

	OrganizationResponseDto getOrganizationById(String id);

	OrganizationResponseDto createOrganization(OrganizationRequestDto organizationRequestDto);

	OrganizationResponseDto updateOrganization(String id, OrganizationRequestDto organizationRequestDto);

	void deleteOrganization(String id);
}
