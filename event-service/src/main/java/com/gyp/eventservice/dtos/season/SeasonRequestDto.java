package com.gyp.eventservice.dtos.season;

import jakarta.validation.constraints.NotNull;

import com.gyp.common.enums.event.SeasonStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
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
