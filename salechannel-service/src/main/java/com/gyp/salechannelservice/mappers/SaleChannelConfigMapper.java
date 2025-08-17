package com.gyp.salechannelservice.mappers;

import com.gyp.common.mappers.AbstractMapper;
import org.mapstruct.Mapper;
import org.mapstruct.MappingInheritanceStrategy;

@Mapper(componentModel = "spring", mappingInheritanceStrategy = MappingInheritanceStrategy.AUTO_INHERIT_FROM_CONFIG)
public interface SaleChannelConfigMapper extends AbstractMapper {
}
