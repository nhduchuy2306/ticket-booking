package com.gyp.eventservice.services;

import java.util.List;

import com.gyp.common.models.EventEventModel;
import com.gyp.eventservice.dtos.event.EventResponseDto;

public interface EventService {
	List<EventEventModel> getListEventModel();

	List<EventResponseDto> getAllEvents();
}
