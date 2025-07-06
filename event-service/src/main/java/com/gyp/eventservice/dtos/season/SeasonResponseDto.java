package com.gyp.eventservice.dtos.season;

import com.gyp.common.enums.event.SeasonStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeasonResponseDto {
	private String id;
	private String name;
	private String description;
	private SeasonStatus status;
}
