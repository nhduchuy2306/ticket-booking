package com.gyp.eventservice.mappers;

import java.time.LocalDateTime;
import java.util.List;

import com.gyp.common.enums.event.TicketStatus;
import com.gyp.common.models.TicketTypeEventModel;
import com.gyp.eventservice.dtos.tickettype.TicketTypeRequestDto;
import com.gyp.eventservice.dtos.tickettype.TicketTypeResponseDto;
import com.gyp.eventservice.entities.TicketTypeEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingInheritanceStrategy;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring",
		mappingInheritanceStrategy = MappingInheritanceStrategy.AUTO_INHERIT_FROM_CONFIG,
		uses = { EventMapper.class })
public interface TicketTypeMapper extends AbstractMapper {
	// To response DTO
	@Mapping(target = "isSaleActive", expression = "java(isSaleActive(entity))")
	@Mapping(target = "isSoldOut", expression = "java(isSoldOut(entity))")
	TicketTypeResponseDto toResponseDto(TicketTypeEntity entity);

	// List mappings
	List<TicketTypeResponseDto> toResponseDtoList(List<TicketTypeEntity> entities);

	// Create new entity from request
	@Mapping(target = "eventEntityList", ignore = true)
	@Mapping(target = "id", ignore = true)
	@Named("toEntity")
	TicketTypeEntity toEntity(TicketTypeRequestDto dto);

	List<TicketTypeEventModel> toEventModelList(List<TicketTypeEntity> ticketTypeEntities);

	// Update existing entity from request
	@Mapping(target = "eventEntityList", ignore = true)
	@Mapping(target = "id", ignore = true)
	@Named("updateEntityFromDto")
	void updateEntityFromDto(TicketTypeRequestDto dto, @MappingTarget TicketTypeEntity entity);

	default boolean isSaleActive(TicketTypeEntity ticketType) {
		if(ticketType.getSaleStartDate() == null || ticketType.getSaleEndDate() == null) {
			return false;
		}
		LocalDateTime now = LocalDateTime.now();
		return ticketType.getSaleStartDate().isBefore(now) &&
				ticketType.getSaleEndDate().isAfter(now) &&
				TicketStatus.AVAILABLE.equals(ticketType.getStatus());
	}

	default boolean isSoldOut(TicketTypeEntity ticketType) {
		return ticketType.getTotalCapacity() != null && ticketType.getTotalCapacity() <= 0;
	}

	@AfterMapping
	default void afterMapping(@MappingTarget TicketTypeEntity entity) {
		mapAbstractFieldsToEntity(entity);
	}

	@AfterMapping
	default void afterMapping(@MappingTarget TicketTypeResponseDto responseDto, TicketTypeEntity entity) {
		mapAbstractFields(entity, responseDto);
	}
}
