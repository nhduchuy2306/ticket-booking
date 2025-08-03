package com.gyp.eventservice.services.impl;

import java.util.List;

import com.gyp.common.dtos.pagination.PaginatedDto;
import com.gyp.common.services.ValidationService;
import com.gyp.common.utils.SecurityUtils;
import com.gyp.common.validators.criteria.ValidationInfo;
import com.gyp.eventservice.dtos.venue.VenueRequestDto;
import com.gyp.eventservice.dtos.venue.VenueResponseDto;
import com.gyp.eventservice.entities.VenueEntity;
import com.gyp.eventservice.exceptions.VenueNotFoundException;
import com.gyp.eventservice.mappers.VenueMapper;
import com.gyp.eventservice.repositories.VenueRepository;
import com.gyp.eventservice.services.VenueService;
import com.gyp.eventservice.services.criteria.VenueSearchCriteria;
import com.gyp.eventservice.services.specifications.VenueSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class VenueServiceImpl implements VenueService {
	private final VenueRepository venueRepository;
	private final ValidationService validationService;
	private final VenueMapper venueMapper;

	@Override
	public List<VenueResponseDto> getAllVenues() {
		var venues = venueRepository.findAll();
		if(venues.isEmpty()) {
			throw new RuntimeException("No venues found");
		}
		return venueMapper.toResponseDtoList(venues);
	}

	@Override
	public List<VenueResponseDto> getAllVenues(VenueSearchCriteria criteria, PaginatedDto pagination) {
		Specification<VenueEntity> seasonSpecification = VenueSpecification.createVenueSearchSpecification(criteria);
		Page<VenueEntity> entities = venueRepository.findAll(seasonSpecification, pagination.toPageable());
		if(!entities.isEmpty()) {
			return venueMapper.toResponseDtoList(entities.getContent());
		}
		return null;
	}

	@Override
	public VenueResponseDto getVenueById(String venueId) throws VenueNotFoundException {
		return venueRepository.findById(venueId)
				.map(venueMapper::toResponseDto)
				.orElseThrow(() -> new VenueNotFoundException("Venue not found with id: " + venueId));
	}

	@Override
	public VenueResponseDto getVenueByName(String venueName) {
		return venueRepository.findByName(venueName)
				.map(venueMapper::toResponseDto)
				.orElseThrow(() -> new RuntimeException("Venue not found with name: " + venueName));
	}

	@Override
	public VenueResponseDto getVenueByLocation(Double latitude, Double longitude) {
		return venueRepository.findByLongitudeAndLatitude(longitude, latitude)
				.map(venueMapper::toResponseDto)
				.orElseThrow(() -> new RuntimeException("Venue not found at location: " + latitude + ", " + longitude));
	}

	@Override
	public VenueResponseDto createVenue(VenueRequestDto venueDto) {
		String organizationId = SecurityUtils.getCurrentOrganizationId();
		VenueEntity venueEntity = venueMapper.toEntity(venueDto);
		venueEntity.setOrganizationId(organizationId);
		venueEntity = venueRepository.save(venueEntity);
		return venueMapper.toResponseDto(venueEntity);
	}

	// TODO: Create validation framework to handle this better
	@Override
	public VenueResponseDto updateVenue(String venueId, VenueRequestDto venueDto) {
		VenueEntity existingVenue = venueRepository.findById(venueId)
				.orElseThrow(() -> new RuntimeException("Venue not found with id: " + venueId));
		venueMapper.updateEntityFromDto(venueDto, existingVenue);
		existingVenue = venueRepository.save(existingVenue);
		return venueMapper.toResponseDto(existingVenue);
	}

	@Override
	public void deleteVenue(String venueId) {
		VenueEntity venueEntity = venueRepository.findById(venueId)
				.orElseThrow(() -> new RuntimeException("Venue not found with id: " + venueId));
		venueRepository.delete(venueEntity);
	}

	@Override
	public List<VenueResponseDto> searchVenues(VenueSearchCriteria criteria) {
		return List.of();
	}

	@Override
	public VenueResponseDto getVenueDetails(String venueId) {
		return null;
	}

	@Override
	public List<VenueResponseDto> getVenuesNearLocation(double lat, double lng, double radius) {
		return List.of();
	}

	@Override
	public ValidationInfo validate(Class<?> clazz) {
		var validationInfo = validationService.extractValidationInfo(clazz);
		if(validationInfo == null) {
			log.warn("No validation info found for request class: {}", clazz.getName());
			return ValidationInfo.empty();
		}
		return validationInfo;
	}
}
