package com.gyp.eventservice.mappers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.gyp.eventservice.dtos.event.EventRequestDto;
import com.gyp.eventservice.dtos.event.EventResponseDto;
import com.gyp.eventservice.dtos.tickettype.TicketTypeResponseDto;
import com.gyp.eventservice.entities.CategoryEntity;
import com.gyp.eventservice.entities.EventEntity;
import com.gyp.eventservice.entities.EventSectionMappingEntity;
import com.gyp.eventservice.entities.SeasonEntity;
import com.gyp.eventservice.entities.TicketTypeEntity;
import com.gyp.eventservice.entities.VenueMapEntity;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingInheritanceStrategy;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring",
		mappingInheritanceStrategy = MappingInheritanceStrategy.AUTO_INHERIT_FROM_CONFIG,
		uses = { EventImageMapper.class, VenueMapMapper.class, TicketTypeMapper.class })
public interface EventMapper extends AbstractMapper {
	// To response DTO
	@Mapping(target = "logoBufferArray", ignore = true)
	@Mapping(target = "season", source = "seasonEntity")
	@Mapping(target = "startTime", source = "time.startTime")
	@Mapping(target = "endTime", source = "time.endTime")
	@Mapping(target = "doorOpenTime", source = "time.doorOpenTime")
	@Mapping(target = "doorCloseTime", source = "time.doorCloseTime")
	@Mapping(target = "note", source = "note")
	@Mapping(target = "venueMap", source = "venueMapEntity")
	@Mapping(target = "categories", source = "categoryEntityList")
	@Mapping(target = "ticketTypes", source = "eventSectionMappingEntityList")
	@Mapping(target = "promotions", source = "eventPromotionEntityList")
	@Mapping(target = "approvals", source = "eventApprovalEntityList")
	@Mapping(target = "ticketsSold", expression = "java(calculateTicketsSold(event))")
	@Mapping(target = "isEventInProgress", expression = "java(isEventInProgress(event))")
	@Mapping(target = "isEventCompleted", expression = "java(isEventCompleted(event))")
	EventResponseDto toResponseDto(EventEntity event);

	// List mappings
	List<EventResponseDto> toResponseDtoList(List<EventEntity> entities);

	// Create new entity from request
	@Mapping(target = "eventImageEntityList", ignore = true)
	@Mapping(target = "logoUrl", ignore = true)
	@Mapping(target = "isGenerated", ignore = true)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "time.startTime", source = "startTime")
	@Mapping(target = "time.endTime", source = "endTime")
	@Mapping(target = "time.doorOpenTime", source = "doorOpenTime")
	@Mapping(target = "time.doorCloseTime", source = "doorCloseTime")
	@Mapping(target = "note", source = "note")
	@Mapping(target = "seasonEntity", source = "seasonId", qualifiedByName = "seasonIdToEntity")
	@Mapping(target = "venueMapEntity", source = "venueMapId", qualifiedByName = "venueMapIdToEntity")
	@Mapping(target = "categoryEntityList", source = "categoryIds", qualifiedByName = "categoryIdsToEntities")
	@Mapping(target = "eventSectionMappingEntityList", source = "ticketTypeIds",
			qualifiedByName = "ticketTypeIdsToMappings")
	@Mapping(target = "eventPromotionEntityList", ignore = true)
	@Mapping(target = "eventApprovalEntityList", ignore = true)
	EventEntity toEntity(EventRequestDto dto);

	// Update existing entity from request
	@Mapping(target = "logoUrl", ignore = true)
	@Mapping(target = "eventImageEntityList", ignore = true)
	@Mapping(target = "isGenerated", ignore = true)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "time.startTime", source = "startTime")
	@Mapping(target = "time.endTime", source = "endTime")
	@Mapping(target = "time.doorOpenTime", source = "doorOpenTime")
	@Mapping(target = "time.doorCloseTime", source = "doorCloseTime")
	@Mapping(target = "note", source = "note")
	@Mapping(target = "seasonEntity", source = "seasonId", qualifiedByName = "seasonIdToEntity")
	@Mapping(target = "venueMapEntity", source = "venueMapId", qualifiedByName = "venueMapIdToEntity")
	@Mapping(target = "categoryEntityList", source = "categoryIds", qualifiedByName = "categoryIdsToEntities")
	@Mapping(target = "eventSectionMappingEntityList", source = "ticketTypeIds",
			qualifiedByName = "ticketTypeIdsToMappings")
	@Mapping(target = "eventPromotionEntityList", ignore = true)
	@Mapping(target = "eventApprovalEntityList", ignore = true)
	void updateEntityFromDto(EventRequestDto dto, @MappingTarget EventEntity entity);

	// Helper methods for calculated fields
	default long calculateTicketsSold(EventEntity event) {
		if(event.getEventSectionMappingEntityList() == null) {
			return 0;
		}
		return event.getEventSectionMappingEntityList().stream()
				.map(EventSectionMappingEntity::getTicketTypeEntity)
				.filter(Objects::nonNull)
				.collect(Collectors.toMap(TicketTypeEntity::getId, ticketType -> ticketType, (left, right) -> left,
						LinkedHashMap::new))
				.values().stream()
				.mapToLong(ticketType -> ticketType.getTotalCapacity() != null ? ticketType.getTotalCapacity() : 0)
				.sum();
	}

	default boolean isEventInProgress(EventEntity event) {
		if(event.getTime() == null || event.getTime().getStartTime() == null ||
				event.getTime().getEndTime() == null) {
			return false;
		}
		LocalDateTime now = LocalDateTime.now();
		return event.getTime().getStartTime().isBefore(now) &&
				event.getTime().getEndTime().isAfter(now);
	}

	default boolean isEventCompleted(EventEntity event) {
		if(event.getTime() == null || event.getTime().getEndTime() == null) {
			return false;
		}
		return event.getTime().getEndTime().isBefore(LocalDateTime.now());
	}

	@Named("seasonIdToEntity")
	default SeasonEntity seasonIdToEntity(String seasonId) {
		if(seasonId == null) {
			return null;
		}
		return SeasonEntity.builder().id(seasonId).build();
	}

	@Named("venueMapIdToEntity")
	default VenueMapEntity venueIdToEntity(String venueMapId) {
		if(StringUtils.isEmpty(venueMapId)) {
			return null;
		}
		return VenueMapEntity.builder().id(venueMapId).build();
	}

	@Named("eventIdToEntity")
	default EventEntity eventIdToEntity(String id) {
		if(id == null) {
			return null;
		}
		return EventEntity.builder().id(id).build();
	}

	@Named("categoryIdsToEntities")
	default List<CategoryEntity> categoryIdsToEntities(List<String> categoryIds) {
		if(categoryIds == null) {
			return new ArrayList<>();
		}
		return categoryIds.stream()
				.map(id -> CategoryEntity.builder().id(id).build())
				.collect(Collectors.toList());
	}

	@Named("ticketTypeIdsToMappings")
	default List<EventSectionMappingEntity> ticketTypeIdsToMappings(List<String> ticketTypeIds) {
		if(ticketTypeIds == null) {
			return new ArrayList<>();
		}
		return ticketTypeIds.stream()
				.filter(StringUtils::isNotBlank)
				.distinct()
				.map(id -> EventSectionMappingEntity.builder()
						.ticketTypeEntity(TicketTypeEntity.builder().id(id).build())
						.build())
				.collect(Collectors.toList());
	}

	@Named("mapToTicketType")
	default TicketTypeEntity map(EventSectionMappingEntity mapping) {
		return mapping.getTicketTypeEntity();
	}

	@IterableMapping(qualifiedByName = "mapToTicketType")
	List<TicketTypeResponseDto> mapTicketTypes(List<EventSectionMappingEntity> mappings);

	@AfterMapping
	default void afterMapping(@MappingTarget EventEntity entity) {
		mapAbstractFieldsToEntity(entity);
	}

	@AfterMapping
	default void afterMapping(@MappingTarget EventResponseDto responseDto, EventEntity entity) {
		mapAbstractFields(entity, responseDto);
	}
}
