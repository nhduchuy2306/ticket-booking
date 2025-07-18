package com.gyp.eventservice.services.impl;

import java.util.List;

import com.gyp.common.exceptions.ResourceNotFoundException;
import com.gyp.eventservice.dtos.venuemap.VenueMapRequestDto;
import com.gyp.eventservice.dtos.venuemap.VenueMapResponseDto;
import com.gyp.eventservice.mappers.VenueMapMapper;
import com.gyp.eventservice.repositories.VenueMapRepository;
import com.gyp.eventservice.services.VenueMapService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VenueMapServiceImpl implements VenueMapService {
	private final VenueMapRepository venueMapRepository;
	private final VenueMapMapper venueMapMapper;

	@Override
	public List<VenueMapResponseDto> getAllVenueMaps() {
		var venueMaps = venueMapRepository.findAll();
		if(!venueMaps.isEmpty()) {
			return venueMaps.stream()
					.map(venueMapMapper::toResponseDto)
					.toList();
		}
		return null;
	}

	@Override
	public VenueMapResponseDto getVenueMapById(String id) {
		var venueMap = venueMapRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Venue map not found with id: " + id));
		return venueMapMapper.toResponseDto(venueMap);
	}

	@Override
	public VenueMapResponseDto createVenueMap(VenueMapRequestDto venueMapRequestDto) {
		var venueMapEntity = venueMapMapper.toEntity(venueMapRequestDto);
		var savedVenueMap = venueMapRepository.save(venueMapEntity);
		return venueMapMapper.toResponseDto(savedVenueMap);
	}

	@Override
	public VenueMapResponseDto updateVenueMap(String id, VenueMapRequestDto venueMapRequestDto) {
		var existingVenueMap = venueMapRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Venue map not found with id: " + id));
		venueMapMapper.updateEntityFromDto(venueMapRequestDto, existingVenueMap);
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
