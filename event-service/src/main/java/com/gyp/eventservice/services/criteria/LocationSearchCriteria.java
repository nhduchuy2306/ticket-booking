package com.gyp.eventservice.services.criteria;

import java.time.LocalDateTime;
import java.util.List;

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
public class LocationSearchCriteria {
	private Double latitude;
	private Double longitude;
	private Double radiusKm;
	private String city;
	private String country;

	// Event-specific location filters
	private LocalDateTime eventDateFrom;
	private LocalDateTime eventDateTo;
	private List<String> categoryIds;

	// Pagination
	@Builder.Default
	private Integer page = 0;

	@Builder.Default
	private Integer size = 20;
	private String organizationId;
}
