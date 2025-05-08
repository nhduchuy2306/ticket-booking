package com.gyp.eventservice.dtos.organizer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizerSummaryDto {
	private String id;
	private String name;
	private String phoneNumber;
}
