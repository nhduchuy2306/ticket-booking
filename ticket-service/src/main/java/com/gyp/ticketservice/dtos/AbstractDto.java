package com.gyp.ticketservice.dtos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AbstractDto {
	private String createUser;
	private String changeUser;
	private LocalDateTime createTimestamp;
	private LocalDateTime changeTimestamp;
}
