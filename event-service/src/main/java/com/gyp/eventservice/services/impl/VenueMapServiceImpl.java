package com.gyp.eventservice.services.impl;

import java.util.List;

import com.gyp.common.dtos.pagination.PaginatedDto;
import com.gyp.common.exceptions.ResourceNotFoundException;
import com.gyp.common.utils.SecurityUtils;
import com.gyp.eventservice.dtos.venuemap.VenueMapRequestDto;
import com.gyp.eventservice.dtos.venuemap.VenueMapResponseDto;
import com.gyp.eventservice.entities.VenueMapEntity;
import com.gyp.eventservice.mappers.VenueMapMapper;
import com.gyp.eventservice.repositories.VenueMapRepository;
import com.gyp.eventservice.services.VenueMapService;
import com.gyp.eventservice.services.criteria.VenueMapSearchCriteria;
import com.gyp.eventservice.services.specifications.VenueMapSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VenueMapServiceImpl implements VenueMapService {
	private final VenueMapRepository venueMapRepository;
	private final VenueMapMapper venueMapMapper;

	@Override
	public List<VenueMapResponseDto> getAllVenueMaps(VenueMapSearchCriteria criteria, PaginatedDto pagination) {
		Specification<VenueMapEntity> specification = VenueMapSpecification.createVenueMapSpecification(criteria);
		var venueMaps = venueMapRepository.findAll(specification, pagination.toPageable());
		if(!venueMaps.isEmpty()) {
			return venueMapMapper.toResponseDtoList(venueMaps.getContent());
		}
		return List.of();
	}

	@Override
	public VenueMapResponseDto getVenueMapById(String id) {
		var venueMap = venueMapRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Venue map not found with id: " + id));
		return venueMapMapper.toResponseDto(venueMap);
	}

	@Override
	public VenueMapResponseDto createVenueMap(VenueMapRequestDto venueMapRequestDto) {
		String organizationId = SecurityUtils.getCurrentOrganizationId();
		var venueMapEntity = venueMapMapper.toEntity(venueMapRequestDto);
		venueMapEntity.setOrganizationId(organizationId);
		var savedVenueMap = venueMapRepository.save(venueMapEntity);
		return venueMapMapper.toResponseDto(savedVenueMap);
	}

	@Override
	public VenueMapResponseDto updateVenueMap(String id, VenueMapRequestDto venueMapRequestDto) {
		String organizationId = SecurityUtils.getCurrentOrganizationId();
		var existingVenueMap = venueMapRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Venue map not found with id: " + id));
		venueMapMapper.updateEntityFromDto(venueMapRequestDto, existingVenueMap);
		existingVenueMap.setOrganizationId(organizationId);
		var updatedVenueMap = venueMapRepository.save(existingVenueMap);
		return venueMapMapper.toResponseDto(updatedVenueMap);
	}

	@Override
	public void deleteVenueMap(String id) {
		var existingVenueMap = venueMapRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Venue map not found with id: " + id));
		venueMapRepository.delete(existingVenueMap);
	}
}
