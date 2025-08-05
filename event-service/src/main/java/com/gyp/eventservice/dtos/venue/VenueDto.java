package com.gyp.eventservice.dtos.venue;

import com.gyp.eventservice.dtos.AbstractDto;
import com.gyp.eventservice.dtos.seatmap.VenueMap;
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
public class VenueDto extends AbstractDto {
	private String id;
	private String name;
	private String address;
	private String city;
	private String country;
	private Integer capacity;
	private Double latitude;
	private Double longitude;
	private VenueMap venueMap;
	private String organizationId;
}
