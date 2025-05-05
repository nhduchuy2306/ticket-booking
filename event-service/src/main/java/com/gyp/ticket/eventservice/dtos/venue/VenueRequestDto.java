package com.gyp.ticket.eventservice.dtos.venue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VenueRequestDto {
	private String name;
	private String address;
	private String city;
	private String country;
	private Integer capacity;
	private Double latitude;
	private Double longitude;
}
