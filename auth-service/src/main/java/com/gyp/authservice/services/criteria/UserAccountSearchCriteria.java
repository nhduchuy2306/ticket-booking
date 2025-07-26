package com.gyp.authservice.services.criteria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAccountSearchCriteria {
	private String organizationId;
	private String sortBy;
}
