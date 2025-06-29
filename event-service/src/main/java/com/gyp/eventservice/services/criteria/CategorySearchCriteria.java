package com.gyp.eventservice.services.criteria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategorySearchCriteria {
	private String id;
	private String name;
	private String description;
	private String sortBy;
}
