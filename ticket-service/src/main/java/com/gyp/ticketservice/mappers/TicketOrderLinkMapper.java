package com.gyp.ticketservice.mappers;

import com.gyp.ticketservice.dtos.ticketorderlink.TicketOrderLinkRequestDto;
import com.gyp.ticketservice.dtos.ticketorderlink.TicketOrderLinkResponseDto;
import com.gyp.ticketservice.entities.TicketEntity;
import com.gyp.ticketservice.entities.TicketOrderLinkEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingInheritanceStrategy;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", mappingInheritanceStrategy = MappingInheritanceStrategy.AUTO_INHERIT_FROM_CONFIG)
public interface TicketOrderLinkMapper extends AbstractMapper {

	@Mapping(target = "id", ignore = true)
	TicketOrderLinkEntity toEntity(TicketOrderLinkRequestDto dto);

	TicketOrderLinkResponseDto toResponse(TicketOrderLinkEntity entity);

	@AfterMapping
	default void afterMapping(@MappingTarget TicketEntity entity) {
		mapAbstractFieldsToEntity(entity);
	}
}
