package com.naas.admin_service.features.working_date_hour.mapper;

import com.naas.admin_service.features.working_date_hour.dto.holiday.HolidayDTO;
import com.naas.admin_service.features.working_date_hour.model.ComCfgSysHolidayDate;
import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface HolidayMapper extends BaseMapper<HolidayDTO,ComCfgSysHolidayDate> {

}
