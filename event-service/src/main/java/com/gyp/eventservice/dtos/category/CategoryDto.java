package com.gyp.eventservice.dtos.category;

import com.gyp.eventservice.dtos.AbstractDto;
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
public class CategoryDto extends AbstractDto {
	private String id;
	private String name;
	private String description;
	private String organizationId;
}
