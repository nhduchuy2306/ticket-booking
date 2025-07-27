package com.gyp.eventservice.dtos.category;

import com.gyp.eventservice.dtos.AbstractDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CategoryDto extends AbstractDto {
	private String id;
	private String name;
	private String description;
	private String organizationId;
}
