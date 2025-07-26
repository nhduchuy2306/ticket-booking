package com.gyp.authservice.services.criteria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserGroupSearchCriteria {
	private String organizationId;
	private String sortBy;
}
