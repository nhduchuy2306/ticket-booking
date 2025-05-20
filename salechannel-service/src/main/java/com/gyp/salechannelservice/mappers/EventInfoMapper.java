package com.gyp.salechannelservice.mappers;

import com.gyp.common.models.EventEventModel;
import com.gyp.salechannelservice.entities.EventInfoEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EventInfoMapper extends AbstractMapper {
	@Mapping(target = "createUser", ignore = true)
	@Mapping(target = "createTimestamp", ignore = true)
	@Mapping(target = "changeUser", ignore = true)
	@Mapping(target = "changeTimestamp", ignore = true)
	EventInfoEntity toEntity(EventEventModel model);

	@AfterMapping
	default void afterMapping(@MappingTarget EventInfoEntity entity) {
		mapAbstractFieldsToEntity(entity);
	}
}
