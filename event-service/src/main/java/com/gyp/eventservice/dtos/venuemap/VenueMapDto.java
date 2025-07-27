package com.gyp.eventservice.dtos.venuemap;

import com.gyp.eventservice.dtos.AbstractDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VenueMapDto extends AbstractDto {
	private String id;
	private String name;
	private Double width;
	private Double height;
	private String venueId;
	private String seatMapId;
	private String organizationId;
}
