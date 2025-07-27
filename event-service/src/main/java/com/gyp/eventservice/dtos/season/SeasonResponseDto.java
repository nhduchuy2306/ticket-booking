package com.gyp.eventservice.dtos.season;

import com.gyp.common.enums.event.SeasonStatus;
import com.gyp.eventservice.dtos.AbstractDto;
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
public class SeasonResponseDto extends AbstractDto {
	private String id;
	private String name;
	private String description;
	private SeasonStatus status;
	private String organizationId;
}
