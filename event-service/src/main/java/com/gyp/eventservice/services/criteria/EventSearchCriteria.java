package com.gyp.eventservice.services.criteria;

import java.time.LocalDateTime;
import java.util.List;

import com.gyp.common.enums.event.EventStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventSearchCriteria {
	// Basic search
	private String keyword; // Search in name, description
	private String organizerId;
	private EventStatus status;

	// Category filtering
	private List<String> categoryIds;

	// Date filtering
	private LocalDateTime startDateFrom;
	private LocalDateTime startDateTo;
	private LocalDateTime endDateFrom;
	private LocalDateTime endDateTo;

	// Location filtering
	private String city;
	private String country;
	private Double latitude;
	private Double longitude;
	private Double radiusKm; // Search radius in kilometers

	// Venue filtering
	private String venueId;
	private Integer minCapacity;
	private Integer maxCapacity;

	// Price filtering (based on ticket types)
	private Double minPrice;
	private Double maxPrice;
	private String organizationId;

	// Pagination
	@Builder.Default
	private Integer page = 0;

	@Builder.Default
	private Integer size = 20;

	// Sorting
	@Builder.Default
	private String sortBy = "startTime"; // startTime, name, createTimestamp

	@Builder.Default
	private String sortDirection = "ASC"; // ASC, DESC

	// Additional filters
	private Boolean hasAvailableTickets;
	private Boolean isPromotionActive;

	public boolean hasLocationFilter() {
		return latitude != null && longitude != null && radiusKm != null;
	}

	public boolean hasDateFilter() {
		return startDateFrom != null || startDateTo != null ||
				endDateFrom != null || endDateTo != null;
	}

	public boolean hasPriceFilter() {
		return minPrice != null || maxPrice != null;
	}
}
