package com.gyp.eventservice.services;

import com.gyp.eventservice.dtos.seatmap.VenueMap;

public interface SeatMapTemplateService {
	VenueMap createTheaterTemplate(String name, int totalRows, int seatsPerRow);

	VenueMap createStadiumConcertTemplate(String name, boolean includeField);

	VenueMap createConferenceTemplate(String name, int numberOfTables, int seatsPerTable);

	VenueMap createArenaConcertTemplate(String name);

	VenueMap createClubTemplate(String name, int standingCapacity);
}
