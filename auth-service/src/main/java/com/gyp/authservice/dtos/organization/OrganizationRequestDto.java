package com.gyp.authservice.dtos.organization;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationRequestDto {
	private String name;
	private String description;
	private String code;
}
