package com.gyp.eventservice.services;

import java.util.List;

import com.gyp.eventservice.dtos.venuemap.VenueMapRequestDto;
import com.gyp.eventservice.dtos.venuemap.VenueMapResponseDto;

public interface VenueMapService {
	List<VenueMapResponseDto> getAllVenueMaps();

	VenueMapResponseDto getVenueMapById(String id);

	VenueMapResponseDto createVenueMap(VenueMapRequestDto venueMapRequestDto);

	VenueMapResponseDto updateVenueMap(String id, VenueMapRequestDto venueMapRequestDto);

	void deleteVenueMap(String id);
}
