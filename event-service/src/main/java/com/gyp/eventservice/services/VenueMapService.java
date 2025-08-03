package com.gyp.eventservice.services;

import java.util.List;

import com.gyp.common.dtos.pagination.PaginatedDto;
import com.gyp.eventservice.dtos.venuemap.VenueMapRequestDto;
import com.gyp.eventservice.dtos.venuemap.VenueMapResponseDto;
import com.gyp.eventservice.services.criteria.VenueMapSearchCriteria;

public interface VenueMapService {

	List<VenueMapResponseDto> getAllVenueMaps(VenueMapSearchCriteria criteria, PaginatedDto pagination);

	VenueMapResponseDto getVenueMapById(String id);

	VenueMapResponseDto createVenueMap(VenueMapRequestDto venueMapRequestDto);

	VenueMapResponseDto updateVenueMap(String id, VenueMapRequestDto venueMapRequestDto);

	void deleteVenueMap(String id);
}
