package com.gyp.eventservice.services;

import java.util.List;

import com.gyp.eventservice.dtos.event.EventResponseDto;
import com.gyp.eventservice.services.criteria.EventSearchCriteria;
import com.gyp.eventservice.services.criteria.LocationSearchCriteria;

public interface EventSearchService {
	List<EventResponseDto> searchEvents(EventSearchCriteria criteria);

	List<EventResponseDto> searchEventsByLocation(LocationSearchCriteria criteria);

	List<EventResponseDto> searchEventsByCategory(List<String> categoryIds);

	List<EventResponseDto> getRecommendedEvents(String organizerId);

	List<EventResponseDto> getTrendingEvents();
}
