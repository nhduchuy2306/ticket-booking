package com.gyp.eventservice.dtos.eventimage;

import com.gyp.eventservice.dtos.AbstractDto;
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
public class EventImageResponseDto extends AbstractDto {
	private String id;
	private String name;
	private String imageUrl;
	private String eventId;
	private String eventName;
}
