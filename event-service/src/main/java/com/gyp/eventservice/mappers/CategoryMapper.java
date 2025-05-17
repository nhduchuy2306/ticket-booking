package com.gyp.eventservice.mappers;

import java.util.List;

import com.gyp.eventservice.dtos.category.CategoryRequestDto;
import com.gyp.eventservice.dtos.category.CategoryResponseDto;
import com.gyp.eventservice.entities.CategoryEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingInheritanceStrategy;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", mappingInheritanceStrategy = MappingInheritanceStrategy.AUTO_INHERIT_FROM_CONFIG)
public interface CategoryMapper extends AbstractMapper {
	// To response DTO
	@Named("toCategoryResponseDto")
	CategoryResponseDto toResponseDto(CategoryEntity entity);

	// List mappings
	@IterableMapping(qualifiedByName = "toCategoryResponseDto")
	List<CategoryResponseDto> toResponseDtoList(List<CategoryEntity> entities);

	// Create new entity from request
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "eventEntityList", ignore = true)
	CategoryEntity toEntity(CategoryRequestDto dto);

	// Update existing entity from request
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "eventEntityList", ignore = true)
	@Named("updateEntityFromDto")
	void updateEntityFromDto(CategoryRequestDto dto, @MappingTarget CategoryEntity entity);

	// Helper for mapping by ID
	@Named("categoryIdToEntity")
	default CategoryEntity categoryIdToEntity(String id) {
		if(id == null) {
			return null;
		}
		CategoryEntity entity = new CategoryEntity();
		entity.setId(id);
		return entity;
	}

	@AfterMapping
	default void afterMapping(@MappingTarget CategoryEntity entity) {
		mapAbstractFieldsToEntity(entity);
	}
}
