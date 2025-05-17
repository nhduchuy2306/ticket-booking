package com.gyp.ticketservice.mappers;

import com.gyp.ticketservice.dtos.tickettype.TicketTypeRequestDto;
import com.gyp.ticketservice.dtos.tickettype.TicketTypeResponseDto;
import com.gyp.ticketservice.entities.TicketEntity;
import com.gyp.ticketservice.entities.TicketTypeEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingInheritanceStrategy;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", mappingInheritanceStrategy = MappingInheritanceStrategy.AUTO_INHERIT_FROM_CONFIG)
public interface TicketTypeMapper extends AbstractMapper {
	@Mapping(target = "eventId", source = "dto.eventId")
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "ticketEntityList", ignore = true)
	TicketTypeEntity toEntity(TicketTypeRequestDto dto);

	TicketTypeResponseDto toResponse(TicketTypeEntity entity);

	@AfterMapping
	default void afterMapping(@MappingTarget TicketEntity entity) {
		mapAbstractFieldsToEntity(entity);
	}
}
