package com.gyp.ticket.eventservice.dtos.organizer;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizerRequestDto {
	private String id;
	private String name;
	private String username;
	private LocalDateTime dob;
	private String phoneNumber;
}
