package com.naas.admin_service.features.working_date_hour.service;

import com.naas.admin_service.features.working_date_hour.dto.working_date.ComCfgSysWorkingDateDto;
import com.naas.admin_service.features.working_date_hour.dto.working_date.WorkingDateResDto;
import com.naas.admin_service.features.working_date_hour.dto.working_date.WorkingDateSearchRequest;
import com.naas.admin_service.features.working_date_hour.dto.working_date.YearInitDto;
import com.naas.admin_service.features.working_date_hour.model.ComCfgSysWorkingDate;
import com.ngvgroup.bpm.core.persistence.service.BaseService;

import java.util.List;

public interface ComCfgSysWorkingDateService extends BaseService<ComCfgSysWorkingDate, WorkingDateResDto> {

    List<WorkingDateResDto> searchWorkingDate(WorkingDateSearchRequest request);

    void registerWorkingDate(ComCfgSysWorkingDateDto request);

    void removeWorkingDate(ComCfgSysWorkingDateDto request);

    void inItWorkingYear(YearInitDto request);
}
