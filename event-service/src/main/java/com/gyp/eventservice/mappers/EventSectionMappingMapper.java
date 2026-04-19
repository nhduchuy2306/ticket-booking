package com.gyp.eventservice.mappers;

import java.util.List;

import com.gyp.eventservice.dtos.eventsectionmapping.EventSectionMappingRequestDto;
import com.gyp.eventservice.dtos.eventsectionmapping.EventSectionMappingResponseDto;
import com.gyp.eventservice.entities.EventEntity;
import com.gyp.eventservice.entities.EventSectionMappingEntity;
import com.gyp.eventservice.entities.SeatMapEntity;
import com.gyp.eventservice.entities.TicketTypeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingInheritanceStrategy;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", mappingInheritanceStrategy = MappingInheritanceStrategy.AUTO_INHERIT_FROM_CONFIG)
public interface EventSectionMappingMapper {

	@Mapping(target = "ticketTypeId", source = "ticketTypeEntity.id")
	@Mapping(target = "seatMapId", source = "seatMapEntity.id")
	@Mapping(target = "eventId", source = "eventEntity.id")
	EventSectionMappingResponseDto toResponseDto(EventSectionMappingEntity entity);

	@Mapping(target = "ticketTypeEntity", source = "dto.ticketTypeId", qualifiedByName = "mapTicketTypeIdToEntity")
	@Mapping(target = "seatMapEntity", source = "dto.seatMapId", qualifiedByName = "mapSeatMapIdToEntity")
	@Mapping(target = "eventEntity", source = "dto.eventId", qualifiedByName = "mapEventIdToEntity")
	EventSectionMappingEntity toEntity(EventSectionMappingRequestDto dto);

	List<EventSectionMappingResponseDto> toResponseDtoList(List<EventSectionMappingEntity> entities);

	@Mapping(target = "ticketTypeEntity", source = "dto.ticketTypeId", qualifiedByName = "mapTicketTypeIdToEntity")
	@Mapping(target = "seatMapEntity", source = "dto.seatMapId", qualifiedByName = "mapSeatMapIdToEntity")
	@Mapping(target = "eventEntity", source = "dto.eventId", qualifiedByName = "mapEventIdToEntity")
	@Mapping(target = "id", ignore = true)
	void updateEntityFromDto(EventSectionMappingRequestDto dto, @MappingTarget EventSectionMappingEntity entity);

	@Named("mapTicketTypeIdToEntity")
	default TicketTypeEntity mapTicketTypeIdToEntity(String ticketTypeId) {
		return TicketTypeEntity.builder().id(ticketTypeId).build();
	}

	@Named("mapSeatMapIdToEntity")
	default SeatMapEntity mapSeatMapIdToEntity(String seatMapId) {
		return SeatMapEntity.builder().id(seatMapId).build();
	}

	@Named("mapEventIdToEntity")
	default EventEntity mapEventIdToEntity(String eventId) {
		return EventEntity.builder().id(eventId).build();
	}
}
