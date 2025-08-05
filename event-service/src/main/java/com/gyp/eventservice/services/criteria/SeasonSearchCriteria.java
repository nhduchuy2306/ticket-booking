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
public class SeasonSearchCriteria {
	private String id;
	private String name;
	private String description;
	private String sortBy;
	private String organizationId;
}
