package com.gyp.ticket.eventservice.dtos.venue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VenueSummaryDto {
	private String id;
	private String name;
	private String address;
	private String city;
	private Integer capacity;
}
