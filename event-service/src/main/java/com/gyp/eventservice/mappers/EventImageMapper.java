package com.gyp.eventservice.mappers;

import java.util.List;

import com.gyp.eventservice.dtos.eventimage.EventImageRequestDto;
import com.gyp.eventservice.dtos.eventimage.EventImageResponseDto;
import com.gyp.eventservice.entities.EventEntity;
import com.gyp.eventservice.entities.EventImageEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingInheritanceStrategy;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", mappingInheritanceStrategy = MappingInheritanceStrategy.AUTO_INHERIT_FROM_CONFIG)
public interface EventImageMapper {

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "eventEntity", source = "eventId", qualifiedByName = "eventIdToEntity")
	EventImageEntity toEntity(EventImageRequestDto dto);

	@Mapping(target = "eventName", source = "eventEntity.name")
	@Mapping(target = "eventId", source = "eventEntity.id")
	EventImageResponseDto toResponseDto(EventImageEntity entity);

	List<EventImageResponseDto> toResponseDtoList(List<EventImageEntity> entities);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "eventEntity", source = "eventId", qualifiedByName = "eventIdToEntity")
	void updateEntityFromDto(EventImageRequestDto dto, @MappingTarget EventImageEntity entity);

	@Named("eventIdToEntity")
	default EventEntity eventIdToEntity(String id) {
		if(id == null) {
			return null;
		}
		EventEntity entity = new EventEntity();
		entity.setId(id);
		return entity;
	}
}
