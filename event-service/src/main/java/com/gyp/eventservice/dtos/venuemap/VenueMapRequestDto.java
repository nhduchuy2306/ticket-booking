package com.gyp.eventservice.dtos.venuemap;

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
public class VenueMapRequestDto {
	private String name;
	private Double width;
	private Double height;
	private String venueId;
	private String seatMapId;
	private String organizationId;
}
