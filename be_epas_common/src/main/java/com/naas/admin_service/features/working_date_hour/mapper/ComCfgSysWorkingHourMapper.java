package com.naas.admin_service.features.working_date_hour.mapper;

import com.naas.admin_service.features.working_date_hour.dto.working_hour.SysWorkingHourDto;
import com.naas.admin_service.features.working_date_hour.model.ComCfgSysWorkingHour;
import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ComCfgSysWorkingHourMapper extends BaseMapper<SysWorkingHourDto, ComCfgSysWorkingHour> {
    ComCfgSysWorkingHourMapper INSTANCE = Mappers.getMapper(ComCfgSysWorkingHourMapper.class);

    @Override
    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL
    )
    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(
            SysWorkingHourDto dto,
            @MappingTarget ComCfgSysWorkingHour entity
    );
}
