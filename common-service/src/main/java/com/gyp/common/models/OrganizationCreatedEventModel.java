package com.gyp.common.models;

import java.io.Serial;
import java.io.Serializable;

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
public class OrganizationCreatedEventModel implements Serializable {
	@Serial
	private static final long serialVersionUID = -6817812645949926242L;

	private String organizationId;
	private String orgSlug;
	private String orgName;
	private String ownerEmail;
}

