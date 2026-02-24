package com.naas.admin_service.features.working_date_hour.service;

import com.naas.admin_service.features.working_date_hour.dto.working_hour.SysWorkingHourDto;
import com.naas.admin_service.features.working_date_hour.dto.working_hour.SysWorkingHourExcel;
import com.naas.admin_service.features.working_date_hour.model.ComCfgSysWorkingHour;
import com.ngvgroup.bpm.core.persistence.service.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ComCfgSysWorkingHourService extends BaseService<ComCfgSysWorkingHour, SysWorkingHourDto> {
    Page<SysWorkingHourDto> search(String keywords, Pageable pageable);

    List<ComCfgSysWorkingHour> create(List<SysWorkingHourDto> req);

    List<SysWorkingHourExcel> exportData(String keyword, Pageable pageable);
}
