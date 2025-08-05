package com.gyp.eventservice.dtos.event;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.NotNull;

import com.gyp.common.enums.event.EventStatus;
import com.gyp.common.intefaces.Request;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestDto implements Request {
	@NotNull
	private String name;

	@Length(min = 10, max = 500)
	private String description;

	@NotNull
	private EventStatus status;

	@NotNull
	private LocalDateTime startTime;

	@NotNull
	private LocalDateTime endTime;

	@NotNull
	private LocalDateTime doorOpenTime;

	@NotNull
	private LocalDateTime doorCloseTime;

	@NotNull
	private String venueMapId;

	@NotNull
	private String seasonId;

	private List<String> categoryIds;
	private String organizationId;
}
