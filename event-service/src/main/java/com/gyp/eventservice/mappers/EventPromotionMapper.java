package com.gyp.eventservice.mappers;

import java.time.LocalDateTime;
import java.util.List;

import com.gyp.eventservice.dtos.eventpromotion.EventPromotionRequestDto;
import com.gyp.eventservice.dtos.eventpromotion.EventPromotionResponseDto;
import com.gyp.eventservice.entities.EventPromotionEntity;
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
public interface EventPromotionMapper extends AbstractMapper {
	// To response DTO
	@Mapping(target = "isActive", expression = "java(isPromotionActive(entity))")
	@Mapping(target = "isExpired", expression = "java(isPromotionExpired(entity))")
	@Named("toEventPromotionResponseDto")
	EventPromotionResponseDto toResponseDto(EventPromotionEntity entity);

	// List mappings
	@IterableMapping(qualifiedByName = "toEventPromotionResponseDto")
	List<EventPromotionResponseDto> toResponseDtoList(List<EventPromotionEntity> entities);

	// Create new entity from request
	@Mapping(target = "id", expression = "java(generateUuid())")
	@Mapping(target = "eventEntity", source = "eventId", qualifiedByName = "eventIdToEntity")
	@Named("toEntity")
	EventPromotionEntity toEntity(EventPromotionRequestDto dto);

	// Update existing entity from request
	@Mapping(target = "id", expression = "java(generateUuid())")
	@Mapping(target = "eventEntity", source = "eventId", qualifiedByName = "eventIdToEntity")
	@Named("updateEntityFromDto")
	void updateEntityFromDto(EventPromotionRequestDto dto, @MappingTarget EventPromotionEntity entity);

	// Helper methods for calculated fields
	default boolean isPromotionActive(EventPromotionEntity promotion) {
		if(promotion.getValidFrom() == null || promotion.getValidTo() == null) {
			return false;
		}
		LocalDateTime now = LocalDateTime.now();
		return promotion.getValidFrom().isBefore(now) && promotion.getValidTo().isAfter(now);
	}

	default boolean isPromotionExpired(EventPromotionEntity promotion) {
		if(promotion.getValidTo() == null) {
			return false;
		}
		return promotion.getValidTo().isBefore(LocalDateTime.now());
	}

	@AfterMapping
	default void afterMapping(@MappingTarget EventPromotionEntity entity) {
		mapAbstractFieldsToEntity(entity);
	}
}
