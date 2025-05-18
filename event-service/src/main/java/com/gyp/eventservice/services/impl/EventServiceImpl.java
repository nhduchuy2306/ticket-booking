package com.gyp.eventservice.services.impl;

import java.util.List;

import com.gyp.common.models.EventEventModel;
import com.gyp.eventservice.entities.EventEntity;
import com.gyp.eventservice.mappers.EventMapper;
import com.gyp.eventservice.repositories.EventRepository;
import com.gyp.eventservice.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
	private final EventRepository eventRepository;
	private final EventMapper eventMapper;

	@Override
	public List<EventEventModel> getListEventModel() {
		List<EventEntity> entities = eventRepository.findAll();
		return eventMapper.toModelList(entities);
	}
}
