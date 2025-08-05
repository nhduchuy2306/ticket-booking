package com.gyp.eventservice.dtos.venuemap;

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
public class VenueMapResponseDto extends AbstractDto {
	private String id;
	private String name;
	private Double width;
	private Double height;
	private String venueId;
	private String venueName;
	private String seatMapId;
	private String seatMapName;
	private String organizationId;
}
