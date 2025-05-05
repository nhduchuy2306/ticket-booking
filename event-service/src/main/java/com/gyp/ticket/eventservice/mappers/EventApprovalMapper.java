package com.gyp.ticket.eventservice.mappers;

import java.util.List;

import com.gyp.ticket.eventservice.dtos.eventapproval.EventApprovalRequestDto;
import com.gyp.ticket.eventservice.dtos.eventapproval.EventApprovalResponseDto;
import com.gyp.ticket.eventservice.entities.EventApprovalEntity;
import com.gyp.ticket.eventservice.entities.EventEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingInheritanceStrategy;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring",
		mappingInheritanceStrategy = MappingInheritanceStrategy.AUTO_INHERIT_FROM_CONFIG,
		uses = { EventMapper.class })
public interface EventApprovalMapper extends AbstractMapper {
	// To response DTO
	@Named("toEventApprovalResponseDto")
	EventApprovalResponseDto toResponseDto(EventApprovalEntity entity);

	// List mappings
	@IterableMapping(qualifiedByName = "toEventApprovalResponseDto")
	List<EventApprovalResponseDto> toResponseDtoList(List<EventApprovalEntity> entities);

	// Create new entity from request
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "eventEntity", source = "eventId", qualifiedByName = "eventIdToEntity")
	@Named("toEntity")
	EventApprovalEntity toEntity(EventApprovalRequestDto dto);

	// Update existing entity from request
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "eventEntity", source = "eventId", qualifiedByName = "eventIdToEntity")
	@Named("updateEntityFromDto")
	void updateEntityFromDto(EventApprovalRequestDto dto, @MappingTarget EventApprovalEntity entity);

	@AfterMapping
	default void afterMapping(@MappingTarget EventEntity entity) {
		mapAbstractFieldsToEntity(entity);
	}
}
