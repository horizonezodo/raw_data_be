package com.naas.admin_service.features.job.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import com.naas.admin_service.features.job.dto.ComCfgJobDto;
import com.naas.admin_service.features.job.model.ComCfgJob;
import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;

@Mapper(componentModel = "spring",nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ComCfgJobMapper extends BaseMapper<ComCfgJobDto, ComCfgJob> {
    ComCfgJobMapper INSTANCE = Mappers.getMapper(ComCfgJobMapper.class);
}
