package com.gyp.eventservice.services;

import java.util.List;

import com.gyp.common.dtos.pagination.PaginatedDto;
import com.gyp.common.exceptions.ResourceNotFoundException;
import com.gyp.common.intefaces.Validatable;
import com.gyp.eventservice.dtos.venue.VenueRequestDto;
import com.gyp.eventservice.dtos.venue.VenueResponseDto;
import com.gyp.eventservice.services.criteria.VenueSearchCriteria;

public interface VenueService extends Validatable {
	List<VenueResponseDto> getAllVenues();

	List<VenueResponseDto> getAllVenues(VenueSearchCriteria criteria, PaginatedDto pagination);

	VenueResponseDto getVenueById(String venueId) throws ResourceNotFoundException;

	VenueResponseDto getVenueByName(String venueName);

	VenueResponseDto getVenueByLocation(Double latitude, Double longitude);

	VenueResponseDto createVenue(VenueRequestDto venueDto);

	VenueResponseDto updateVenue(String venueId, VenueRequestDto venueDto);

	void deleteVenue(String venueId);

	List<VenueResponseDto> searchVenues(VenueSearchCriteria criteria);

	VenueResponseDto getVenueDetails(String venueId);

	List<VenueResponseDto> getVenuesNearLocation(double lat, double lng, double radius);
}
