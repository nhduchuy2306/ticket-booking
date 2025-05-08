package com.gyp.ticketservice.mappers;

import com.gyp.ticketservice.dtos.ticket.TicketRequestDto;
import com.gyp.ticketservice.dtos.ticket.TicketResponseDto;
import com.gyp.ticketservice.entities.TicketEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingInheritanceStrategy;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", mappingInheritanceStrategy = MappingInheritanceStrategy.AUTO_INHERIT_FROM_CONFIG)
public interface TicketMapper extends AbstractMapper {

	@Mapping(target = "id", expression = "java(generateUuid())")
	TicketEntity toEntity(TicketRequestDto dto);

	TicketResponseDto toResponse(TicketEntity entity);

	@AfterMapping
	default void afterMapping(@MappingTarget TicketEntity entity) {
		mapAbstractFieldsToEntity(entity);
	}
}
