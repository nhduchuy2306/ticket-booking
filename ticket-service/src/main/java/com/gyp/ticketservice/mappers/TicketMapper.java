package com.gyp.ticketservice.mappers;

import java.util.List;

import com.gyp.ticketservice.dtos.ticket.TicketRequestDto;
import com.gyp.ticketservice.dtos.ticket.TicketResponseDto;
import com.gyp.ticketservice.dtos.tickettype.TicketTypeSummaryDto;
import com.gyp.ticketservice.entities.TicketEntity;
import com.gyp.ticketservice.entities.TicketTypeEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingInheritanceStrategy;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", mappingInheritanceStrategy = MappingInheritanceStrategy.AUTO_INHERIT_FROM_CONFIG)
public interface TicketMapper extends AbstractMapper {

	@Mapping(target = "reservedDateTime", source = "dto.reservedDateTime")
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "ticketTypeEntity", ignore = true)
	TicketEntity toEntity(TicketRequestDto dto);

	@Mapping(target = "ticketTypeSummaryDto", source = "entity.ticketTypeEntity",
			qualifiedByName = "mapTicketTypeSummary")
	@Mapping(target = "ticketTypeId", source = "entity.ticketTypeEntity.id")
	TicketResponseDto toResponse(TicketEntity entity);

	List<TicketResponseDto> toResponses(List<TicketEntity> entities);

	@Named("mapTicketTypeSummary")
	default TicketTypeSummaryDto mapTicketTypeSummary(TicketTypeEntity entity) {
		TicketTypeSummaryDto ticketTypeSummaryDto = new TicketTypeSummaryDto();
		ticketTypeSummaryDto.setName(entity.getName());
		ticketTypeSummaryDto.setDescription(entity.getDescription());
		return ticketTypeSummaryDto;
	}

	@AfterMapping
	default void afterMapping(@MappingTarget TicketEntity entity) {
		mapAbstractFieldsToEntity(entity);
	}
}
