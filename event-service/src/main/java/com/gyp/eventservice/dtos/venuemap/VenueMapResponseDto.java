package com.gyp.eventservice.dtos.venuemap;

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
public class VenueMapResponseDto extends AbstractDto {
	private String id;
	private String name;
	private Double width;
	private Double height;
}
