package com.naas.admin_service.features.working_date_hour.mapper;

import com.naas.admin_service.features.working_date_hour.dto.working_date.WorkingDateResDto;
import com.naas.admin_service.features.working_date_hour.model.ComCfgSysWorkingDate;
import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface WorkingDateMapper extends BaseMapper<WorkingDateResDto, ComCfgSysWorkingDate> {

}
