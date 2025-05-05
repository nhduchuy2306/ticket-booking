package com.gyp.ticket.eventservice.dtos.organizer;

import java.time.LocalDateTime;

import com.gyp.ticket.eventservice.dtos.AbstractDto;
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
public class OrganizerResponseDto extends AbstractDto {
	private String id;
	private String name;
	private String username;
	private LocalDateTime dob;
	private String phoneNumber;
	private Integer age;

	// Optional
	//	private List<EventSummaryDto> events;
}
