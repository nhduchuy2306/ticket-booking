package com.gyp.ticket.eventservice.mappers;

import com.gyp.ticket.eventservice.dtos.event.EventRequestDto;
import com.gyp.ticket.eventservice.dtos.event.EventResponseDto;
import com.gyp.ticket.eventservice.entities.EventEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EventMapper {
	EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

	EventEntity toEntity(EventRequestDto dto);

	EventResponseDto toDto(EventEntity eventEntity);
}
