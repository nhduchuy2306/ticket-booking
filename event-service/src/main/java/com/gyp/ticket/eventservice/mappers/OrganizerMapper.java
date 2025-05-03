package com.gyp.ticket.eventservice.mappers;

import com.gyp.common.models.UserAccountModel;
import com.gyp.ticket.eventservice.entities.OrganizerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrganizerMapper {
	OrganizerMapper INSTANCE = Mappers.getMapper(OrganizerMapper.class);

	@Mapping(target = "eventEntityList", ignore = true)
	OrganizerEntity toEntity(UserAccountModel model);
}
