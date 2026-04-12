package com.gyp.authservice.dtos.organization;

import java.io.Serial;

import com.gyp.authservice.dtos.AbstractDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationResponseDto extends AbstractDto {
	@Serial
	private static final long serialVersionUID = 222452602074102819L;

	private String id;
	private String name;
	private String description;
	private String code;
}
