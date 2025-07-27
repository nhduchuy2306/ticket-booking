package com.gyp.eventservice.dtos.season;

import jakarta.validation.constraints.NotNull;

import com.gyp.common.enums.event.SeasonStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeasonRequestDto {
	@NotNull
	private String name;
	private String description;
	private SeasonStatus status;
	private String organizationId;
}
