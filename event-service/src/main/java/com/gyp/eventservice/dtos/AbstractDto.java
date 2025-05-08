package com.gyp.eventservice.dtos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AbstractDto {
	private String createUser;
	private String changeUser;
	private LocalDateTime createTimestamp;
	private LocalDateTime changeTimestamp;
}
