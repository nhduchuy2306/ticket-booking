package com.gyp.eventservice.dtos.season;

import com.gyp.common.enums.event.SeasonStatus;
import com.gyp.eventservice.dtos.AbstractDto;
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
public class SeasonDto extends AbstractDto {
	private String id;
	private String name;
	private String description;
	private SeasonStatus status;
	private String organizationId;
}
