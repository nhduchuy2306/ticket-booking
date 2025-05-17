package com.gyp.ticketservice.mappers;

import com.gyp.ticketservice.dtos.ticketgeneration.TicketGenerationRequestDto;
import com.gyp.ticketservice.dtos.ticketgeneration.TicketGenerationResponseDto;
import com.gyp.ticketservice.dtos.ticketgeneration.TicketGenerationSummaryDto;
import com.gyp.ticketservice.entities.TicketEntity;
import com.gyp.ticketservice.entities.TicketGenerationEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingInheritanceStrategy;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", mappingInheritanceStrategy = MappingInheritanceStrategy.AUTO_INHERIT_FROM_CONFIG)
public interface TicketGenerationMapper extends AbstractMapper {

	@Mapping(target = "id", ignore = true)
	TicketGenerationEntity toEntity(TicketGenerationRequestDto dto);

	TicketGenerationResponseDto toResponse(TicketGenerationEntity entity);

	TicketGenerationSummaryDto toSummary(TicketGenerationEntity ticketGenerationEntity);

	@AfterMapping
	default void afterMapping(@MappingTarget TicketEntity entity) {
		mapAbstractFieldsToEntity(entity);
	}

}
