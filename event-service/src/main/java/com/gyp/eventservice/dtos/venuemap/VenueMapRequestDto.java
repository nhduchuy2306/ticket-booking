package com.gyp.eventservice.dtos.venuemap;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VenueMapRequestDto {
	private String name;
	private Double width;
	private Double height;
	private String venueId;
}
