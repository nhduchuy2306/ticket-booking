package com.gyp.ticket.eventservice.dtos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrganizerDto {
	private String id;
	private String name;
	private String username;
	private LocalDateTime dob;
	private String phoneNumber;
}
