package com.gyp.eventservice.services;

import java.util.List;

import com.gyp.eventservice.exceptions.VenueNotFoundException;
import com.gyp.eventservice.services.criteria.VenueSearchCriteria;
import com.gyp.eventservice.dtos.venue.VenueRequestDto;
import com.gyp.eventservice.dtos.venue.VenueResponseDto;

public interface VenueService {
	List<VenueResponseDto> getVenues();

	VenueResponseDto getVenueById(String venueId) throws VenueNotFoundException;

	VenueResponseDto getVenueByName(String venueName);

	VenueResponseDto getVenueByLocation(Double latitude, Double longitude);

	VenueResponseDto createVenue(VenueRequestDto venueDto);

	VenueResponseDto updateVenue(String venueId, VenueRequestDto venueDto);

	void deleteVenue(String venueId);

	List<VenueResponseDto> searchVenues(VenueSearchCriteria criteria);

	VenueResponseDto getVenueDetails(String venueId);

	List<VenueResponseDto> getVenuesNearLocation(double lat, double lng, double radius);
}
