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
public class EventImageRequestDto {
	private String name;
	private String imageUrl;
	private String eventId;
}
