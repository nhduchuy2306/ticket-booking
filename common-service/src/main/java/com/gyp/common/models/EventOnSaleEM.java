package com.gyp.common.models;

import com.gyp.common.enums.event.EventStatus;
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
public class EventOnSaleEM {
	private EventStatus status;
	private String eventId;
}
