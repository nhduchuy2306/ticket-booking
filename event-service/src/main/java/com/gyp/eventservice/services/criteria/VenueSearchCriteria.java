package com.gyp.eventservice.services.criteria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VenueSearchCriteria {
	private String keyword; // Search in name, address
	private String city;
	private String country;

	// Location-based search
	private Double latitude;
	private Double longitude;
	private Double radiusKm;

	// Capacity filtering
	private Integer minCapacity;
	private Integer maxCapacity;

	// Pagination and sorting
	@Builder.Default
	private Integer page = 0;

	@Builder.Default
	private Integer size = 20;

	@Builder.Default
	private String sortBy = "name";

	@Builder.Default
	private String sortDirection = "ASC";
	private String organizationId;
}
