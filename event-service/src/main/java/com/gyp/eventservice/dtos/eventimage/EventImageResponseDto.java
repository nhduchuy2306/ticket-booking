package com.gyp.eventservice.dtos.eventimage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventImageResponseDto {
	private String id;
	private String name;
	private String imageUrl;
	private String eventId;
	private String eventName;
}
