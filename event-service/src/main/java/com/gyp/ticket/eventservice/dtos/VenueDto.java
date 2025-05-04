package com.gyp.ticket.eventservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VenueDto {
	private String id;
	private String name;
	private String address;
	private String city;
	private String country;
	private Integer capacity;
	private Double latitude;
	private Double longitude;
}
